/**
 * 
 */
package com.hitech.dms.web.controller.activityClaim.inv.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.hitech.dms.web.dao.activityClaim.inv.search.dao.ActivityClaimInvSearchDao;
import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimAutoSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimInvSearchRequest;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimAutoSearchResponseModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimInvSearchResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimInvSearchController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvSearchController.class);

	@Autowired
	private ActivityClaimInvSearchDao activityClaimInvSearchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchActivityClaimInvSearchList")
	public ResponseEntity<?> fetchActivityClaimInvSearchList(@RequestBody ActivityClaimInvSearchRequest requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityClaimInvSearchResponseModel> responseModelList = activityClaimInvSearchDao
				.fetchActivityClaimInvSearchList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Claim Invoice Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping(value = "/fetchActivityClaimInvAutoSearchList")
	public ResponseEntity<?> fetchActivityClaimInvAutoSearchList(
			@RequestBody ActivityClaimAutoSearchRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityClaimAutoSearchResponseModel> responseModelList = activityClaimInvSearchDao
				.fetchActivityClaimInvAutoSearchList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription(
					"Fetch Activity Claim Invoice Auto Search Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Auto Search Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Auto Search Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
