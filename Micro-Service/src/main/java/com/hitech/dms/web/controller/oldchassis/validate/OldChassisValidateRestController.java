package com.hitech.dms.web.controller.oldchassis.validate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.oldchassis.validate.OldChassisValidateDao;
import com.hitech.dms.web.model.oldchassis.validate.request.OldChassisValidateRequestModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisValidateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/oldChassis")
@SecurityRequirement(name = "hitechApis")
public class OldChassisValidateRestController {

private static final Logger logger = LoggerFactory.getLogger(OldChassisValidateRestController.class);
	
	@Autowired
	private OldChassisValidateDao OldChassisValidateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/fetchOldChassisDTLList/{vinId}")
	public ResponseEntity<?> fetchOldChassisDTLList(@PathVariable BigInteger vinId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		OldChassisDTLResponseModel responseModelList = OldChassisValidateDao.fetchOldChassisDTLList(userCode, vinId);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Chassis Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	@PostMapping("/fetchOldChassisDetailsValidate")
	public ResponseEntity<?> fetchOldChassisDetailsList(@RequestBody OldChassisValidateRequestModel oldChassisValidateRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		OldChassisValidateResponseModel responseModelList = OldChassisValidateDao.fetchOldChassisDTLList(userCode,oldChassisValidateRequestModel);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Old Chassis DTL on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Validated Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Not Validated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}	
}
