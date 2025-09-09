/**
 * 
 */
package com.hitech.dms.web.controller.activitylog;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogRequestModel;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;
import com.hitech.dms.web.service.activitylog.UserActivityLogService;
import com.hitech.dms.web.service.user.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/log")
@SecurityRequirement(name = "hitechApis")
public class UserActivityLogController {
	private static final Logger logger = LoggerFactory.getLogger(UserActivityLogController.class);

	@Autowired
	private UserActivityLogService activityLogService;
	@Autowired
	private UserService userService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/updateActivityLogs")
	public ResponseEntity<?> updateActivityLogs(@Valid @RequestBody UserActivityLogRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println(userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		UserActivityLogResponseModel responseModel = activityLogService.addActivityLog(userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("User Activity Log created on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Activity Log Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/addAppAccessLog")
	public ResponseEntity<?> addAppAccessLog(@Valid @RequestBody AccessLogRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		UserActivityLogResponseModel responseModel = null;
		responseModel = userService.insertIntoAccessLog(userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("User Access Log created on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Access Log Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/updateAppAccessLog")
	public ResponseEntity<?> updateAppAccessLog(@Valid @RequestBody AccessLogUpdateRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		UserActivityLogResponseModel responseModel = null;
		responseModel = userService.updateAccessLog(userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("User Access Log updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Access Log Not Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
