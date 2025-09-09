package com.hitech.dms.web.Controller.spare.targetSetting;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.targetSetting.TargetSettingDao;
import com.hitech.dms.web.model.spare.SparePartUploadResponseModel;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.model.targetSetting.request.TargetSettingRequestModel;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingResponseModel;
import com.hitech.dms.web.service.targetSetting.TargetSettingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/target/setting")
@SecurityRequirement(name = "hitechApis")
public class TargetSettingController {

	@Value("${file.upload-dir.StockTemplate:C:\\VST-DMS-APPS\\template\\spare template\\\\}")
    private String templateDownloadPath;
	
	
	@Autowired
	TargetSettingService targetSettingService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/targetFor")
	public ResponseEntity<?> fetchTargetFor(@RequestParam() String userType, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> targetForList = targetSettingService.fetchTargetFor(userType);
		if (targetForList != null && !targetForList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Target for List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Target for List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(targetForList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getProductCategory")
	public ResponseEntity<?> getProductCategory(@RequestParam(required = false) BigInteger partyCategoryId ,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoCategoryResponse> responseModelList = targetSettingService.getProductCategory(partyCategoryId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/upload")
	public ResponseEntity<?> upload(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart() TargetSettingRequestModel targetSettingRequestModel, 
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
		TargetSettingResponseModel responseModel = null;
		if (isFileExist) {
			responseModel = targetSettingService.uploadExcel(authorizationHeader, userCode,
					targetSettingRequestModel, file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Target Setting .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Target Setting .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/submit")
	public ResponseEntity<?> saveTargetData(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart() TargetSettingRequestModel targetSettingRequestModel, 
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
		TargetSettingResponseModel responseModel = null;
		if (isFileExist) {
			responseModel = targetSettingService.submitTargetData(authorizationHeader, userCode,
					targetSettingRequestModel, file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Target Setting .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Target Setting .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/downloadTemplate")
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
