package com.hitech.dms.web.controller.admin.village.add;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.admin.village.request.VillageRequest;
import com.hitech.dms.web.model.admin.village.response.VillageResponse;
import com.hitech.dms.web.model.admin.village.response.VillageUploadExcelRes;
import com.hitech.dms.web.service.admin.village.add.VillageService;

@RequestMapping("/api/v1/village")
@RestController
public class AddVillageController {
	
	
	@Value("${file.upload-dir.AddVillageTemp}")
	private String templateDownloadPath;


	@Autowired
	private VillageService villageService;
	
	@PostMapping(value = "/save")
	public ResponseEntity<?> addVillage(@Valid @RequestBody VillageRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		VillageResponse responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare apr return not created or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		 responseModel = villageService.addVillage(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping(value = "/addVillageTemplate")
	public ResponseEntity downloadTemplate(@RequestParam("filename") String filename, HttpServletResponse response,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		Resource resource = null;
		try {
		
			 String filePath = System.getProperty("os.name").contains("Windows") ? templateDownloadPath : "/var/VST-DMS-APPS/FILES/Template/add-village/";

			Path path = Paths.get(filePath + filename);
			resource = new UrlResource(path.toUri());
			response.setContentType("application/octet-stream");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(resource);

	}
	
	
	@PostMapping("/uploadAddVillageXlxs")
	public ResponseEntity<?> uploadMslSpare(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart(required = false) MultipartFile file, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		boolean isFileExist = true;
		if (file == null || file.isEmpty()) {

			isFileExist = false;
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		VillageUploadExcelRes responseModel = null;
		if (isFileExist) {
			responseModel = villageService.addVillageUploadedFile(authorizationHeader, userCode,  file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + LocalDate.now());
				codeResponse.setMessage(responseModel.getMsg());
			}else if(responseModel.getStatusCode().compareTo(WebConstants.STATUS_EXPECTATION_FAILED_417) == 0) {
				codeResponse.setCode("EC417");
				codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
				codeResponse.setMessage(responseModel.getMsg());
			  }
				else {
				codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
				codeResponse.setMessage("Invalid data in excel sheet .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Please Upload Spare Part .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
		
	

	
	
	
}
