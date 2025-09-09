package com.hitech.dms.web.controller.activity.source.form;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.source.form.dao.ActivitySourceFormDao;
import com.hitech.dms.web.model.activity.source.form.request.ActivityFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.request.SourceFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.response.ActivitySourceFormResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class ActivitySourceFormController {
	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceFormController.class);

	@Autowired
	private ActivitySourceFormDao activitySourceFormDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/saveEnqActivity")
	public ResponseEntity<?> saveActivity(@RequestBody ActivityFormRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivitySourceFormResponseModel responseModel = activitySourceFormDao.saveActivity(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Created Activity Source on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source Successfully Created");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Activity Source or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/saveEnqSource")
	public ResponseEntity<?> saveActivity(@RequestBody SourceFormRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivitySourceFormResponseModel responseModel = activitySourceFormDao.saveSource(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Created Source on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Source Successfully Created");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Enquiry Source or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
