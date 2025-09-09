package com.hitech.dms.web.controller.activityClaim.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activityClaim.create.ActivityClaimCreateDao;
import com.hitech.dms.web.entity.activityClaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activityClaim.create.request.ActivityClaimHdrDtlRequestModel;
import com.hitech.dms.web.model.activityClaim.create.request.FetchActivityPlanNoAutoRequestMode;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrDtlResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityPlanAutoResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimCreateController {

	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimCreateController.class);

	@Autowired
	private ActivityClaimCreateDao activityClaimCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchActivityPlanNoAuto")
	public ResponseEntity<?> getActivityPlanNoAuto(
			@RequestBody FetchActivityPlanNoAutoRequestMode planNoAutoRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<ActivityPlanAutoResponseModel> paymentTypeList = activityClaimCreateDao.activityPlanAuto(userCode,
				planNoAutoRequestModel.getDealerId(), planNoAutoRequestModel.getIsFor(),
				planNoAutoRequestModel.getSearchText());
		if (paymentTypeList != null && !paymentTypeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Plan No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan No. Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(paymentTypeList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchActivityHdrAndDtl")
	public ResponseEntity<?> getActivityHdrAndDtl(@RequestBody ActivityClaimHdrDtlRequestModel acHdrDtlrequestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ActivityClaimHdrDtlResponseModel responseModel = activityClaimCreateDao.fetchActivityHdrAndDtl(userCode,
				acHdrDtlrequestModel.getDealerId(), acHdrDtlrequestModel.getId(),
				acHdrDtlrequestModel.getIsFor(), device);

		if (responseModel.getActDtl() != null || responseModel.getActHdr() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Hdr and Dtl on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Hdr and Dtl fetch Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Hdr and Dtl Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/submitActivityClaim")
	public ResponseEntity<?> submitActivityClaim(@Valid @RequestPart ActivityClaimHdrEntity requestedData,
			@RequestPart(required = false) List<MultipartFile> files, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityClaimCreateResponseModel responseModel = null;
		try {
			responseModel = activityClaimCreateDao.createActivityClaim(userCode, requestedData, files, device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Activity Claim Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				//codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Activity Claim  Not Create or server side error.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
