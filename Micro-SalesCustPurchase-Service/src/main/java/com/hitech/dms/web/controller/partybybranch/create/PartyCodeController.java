package com.hitech.dms.web.controller.partybybranch.create;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.allotment.create.MachineAllotCreateDao;
import com.hitech.dms.web.dao.partybybranch.create.PartyCodeCreateDao;
import com.hitech.dms.web.model.allot.create.request.MachineAllotCreateRequestModel;
import com.hitech.dms.web.model.allot.create.response.MachineAllotCreateResponseModel;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.response.PartyCodeCreateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/partycode")
@SecurityRequirement(name = "hitechApis")
public class PartyCodeController {

	private static final Logger logger = LoggerFactory.getLogger(PartyCodeController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@Autowired
	private PartyCodeCreateDao partyCreateDao;
	
	@PostMapping(value = "/create")
	public ResponseEntity<?> create(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@ModelAttribute PartyCodeCreateRequestModel requestModel, BindingResult bindingResult,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
	//	System.out.println("at controller all data is "+requestModel.toString());
		System.out.println("the request at controller is "+requestModel);
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyCodeCreateResponseModel responseModel = null;
	   responseModel = partyCreateDao.create(authorizationHeader, userCode, requestModel);

//		if (bindingResult.hasErrors()) {
//			codeResponse.setCode("EC400");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Machine Allotment not created or server side error.");
//
//			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
//			errorDetails.setCount(bindingResult.getErrorCount());
//			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
//			List<String> errors = new ArrayList<>();
//			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
//			errorDetails.setErrors(errors);
//
//			userAuthResponse.setResponseCode(codeResponse);
//			userAuthResponse.setResponseData(errorDetails);
//
//			return ResponseEntity.ok(userAuthResponse);
//		}
		System.out.println("at controller response "+responseModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating partycode.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
