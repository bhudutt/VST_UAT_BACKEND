/**
 * 
 */
package com.hitech.dms.web.controller.aop.template.submit;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.aop.template.submit.AopTargetSubmitDao;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetSubmitRequestModel;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetUpdateRequestModel;
import com.hitech.dms.web.model.aop.template.submit.response.AopTargetSubmitResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/aop")
@SecurityRequirement(name = "hitechApis")
public class AopTargetSubmitController {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetSubmitController.class);

	@Autowired
	private AopTargetSubmitDao dao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody @Valid AopTargetSubmitRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		AopTargetSubmitResponseModel responseModel = dao.submitAopTarget(authorizationHeader, userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("AOP Target submitted on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("AOP Target Not Submitted Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
	
	@PostMapping(value = "/update")
	public ResponseEntity<?> update(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody @Valid AopTargetUpdateRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		AopTargetSubmitResponseModel responseModel = dao.updateAopTarget(authorizationHeader, userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("AOP Target Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("AOP Target Not Updated Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}

}
