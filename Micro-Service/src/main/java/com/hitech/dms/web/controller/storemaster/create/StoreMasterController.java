package com.hitech.dms.web.controller.storemaster.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.storemaster.create.StoreMasterCreateDao;
import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterFormRequestModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;
import com.hitech.dms.web.model.storemaster.create.response.StroeSearchResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/storemaster")
@SecurityRequirement(name = "hitechApis")
public class StoreMasterController {

	private static final Logger logger = LoggerFactory.getLogger(StoreMasterController.class);
	
	@Autowired
	private StoreMasterCreateDao storeMasterCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@PostMapping(value = "/createStoreMaster")
	public ResponseEntity<?> createStoreMaster(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody StoreMasterFormRequestModel requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		StoreMasterCreateResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Store Master not created or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = storeMasterCreateDao.createStoreMaster(authorizationHeader, userCode, requestModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Store Master.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value = "/searchStoreMaster")
	public ResponseEntity<?> searchStoreMaster(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer page, @RequestParam() Integer size,@RequestParam () Integer dealerId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		StroeSearchResponseModel responseModel = null;
		
		List<StroeSearchResponseModel> responseModelList = storeMasterCreateDao.searchStoreMaster(authorizationHeader, userCode,page,size,dealerId);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Fetching Store Data.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/update")
	public ResponseEntity<?> updateBayMaster(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer id, @RequestParam() String isActive, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		StoreMasterCreateResponseModel storeMasterResponse = storeMasterCreateDao.updateStoreMaster(userCode, id, isActive);
		if (storeMasterResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Store Master Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(storeMasterResponse.getMsg());
		} else if (storeMasterResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Store Master Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(storeMasterResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
