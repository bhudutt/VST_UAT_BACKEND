package com.hitech.dms.web.spare.controller.upload;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.spare.dao.SparePartUploadDao;
import com.hitech.dms.web.spare.model.SparePartUploadResponseModel;
import com.hitech.dms.web.spare.model.SpareUploadRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/spare")
@SecurityRequirement(name = "hitechApis")
public class SpareUploadController {

	@Value("${file.upload-dir.StockTemplate:C:\\VST-DMS-APPS\\template\\spare template\\\\}")
    private String templateDownloadPath;
	
	
	@Autowired
	SparePartUploadDao sparePartUploadDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/uploadMsl")
	public ResponseEntity<?> uploadMslSpare(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer dealer,
			@RequestParam() Integer branch,
			@RequestPart(required = false) MultipartFile file,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
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
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePartUploadResponseModel responseModel = null;
		if (isFileExist) {
			responseModel = sparePartUploadDao.validateUploadedFile(authorizationHeader, userCode,
					branch, file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Spare Part .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Spare Part .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value = "/downloadTemplateOfUploadMsl")
	public ResponseEntity downloadTemplate(@RequestParam("filename") String filename,
			HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		Resource resource = null;
		try {
			String filePath="";
			String property = System.getProperty("os.name");
			if(property.contains("Windows")) {
				filePath=templateDownloadPath;
			}else {
				filePath="/var/VST-DMS-APPS/FILES/Template/Spare Template/";
			}
			
			Path path = Paths.get( filePath+ filename);
			resource = new UrlResource(path.toUri());
			response.setContentType("application/octet-stream");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(resource);

	}
	
	
}
