/**
 * 
 */
package com.hitech.dms.web.controller.activityClaim.inv.dtl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.activityClaim.inv.dao.ActivityClaimInvDtlDao;
import com.hitech.dms.web.model.activityClaim.invoice.request.ActivityClaimInvRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.response.ActivityClaimInvHdrResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimInvDtlController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvDtlController.class);

	@Autowired
	private ActivityClaimInvDtlDao activityClaimInvDtlDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchActivityClaimInvDTL")
	public ResponseEntity<?> fetchActivityClaimInvDTL(@RequestBody ActivityClaimInvRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		System.out.println("userController "+requestModel);
		ActivityClaimInvHdrResponseModel responseModel = activityClaimInvDtlDao.fetchActivityClaimInvDTL(userCode,
				requestModel);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Claim Invoice Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Claim Invoice Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Claim Invoice Detail Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
