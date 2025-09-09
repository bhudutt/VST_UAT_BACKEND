package com.hitech.dms.web.controller.activityClaim.aproval;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.activityClaim.approval.ActivityClaimApprovalDao;
import com.hitech.dms.web.model.activityClaim.approval.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimHdrDtlViewResponseModel;
import com.hitech.dms.web.model.activityClaim.create.request.ActivityClaimHdrDtlRequestModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrDtlResponseModel;
import com.hitech.dms.web.model.activityplan.approval.request.ActivityPlanApprovalRequestModel;
import com.hitech.dms.web.model.activityplan.approval.response.ActivityPlanApprovalResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 * @Edited Ram
 *
 */

@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimApprovalController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimApprovalController.class);

	@Autowired
	private ActivityClaimApprovalDao activityClaimApprovalDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchActivityClaimNoDtl")
	public ResponseEntity<?> fetchActivityClaimApproval(@RequestBody ActivityClaimHdrDtlRequestModel acHdrDtlrequestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ActivityClaimHdrDtlViewResponseModel responseModel = activityClaimApprovalDao.fetchActivityHdrAndDtlView(userCode,
				acHdrDtlrequestModel.getDealerId(), acHdrDtlrequestModel.getId(),acHdrDtlrequestModel.getIsFor(), device);

		if (responseModel.getActDtl() != null || responseModel.getActHdr() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity claim approval data on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity claim approval Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity claim approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/approveRejectActivityClaim")
	public ResponseEntity<?> approveRejectActivityClaim(@RequestBody ActivityClaimApproveRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		//System.out.println("the value we get is "+requestModel);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityClaimApproveResponse responseModel = activityClaimApprovalDao.approveRejectActivityClaim(userCode,requestModel, device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Claim Approval Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Approval Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}


}
