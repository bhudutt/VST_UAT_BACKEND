package com.hitech.dms.web.controller.activityClaim.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activityClaim.search.ActivityClaimSearchDao;
import com.hitech.dms.web.model.activityClaim.search.request.ActivityClaimSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.search.response.ActivityClaimSearchMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */
@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimSearchController.class);
	
	@Autowired
	private ActivityClaimSearchDao activityClaimSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	
	@PostMapping("/fetchActivityClaimSearch")
	public ResponseEntity<?> fetchActivityClaimSearch(@RequestBody ActivityClaimSearchRequestModel acRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ActivityClaimSearchMainResponseModel responseModel = activityClaimSearchDao.fetchActivityClaimSearchList(userCode,
				acRequestModel);
		if (responseModel != null && !responseModel.getAcSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Claim Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}


}
