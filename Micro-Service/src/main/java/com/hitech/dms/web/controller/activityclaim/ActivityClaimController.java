/**
 * 
 */
package com.hitech.dms.web.controller.activityclaim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.entity.activityclaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityPermissionRequestModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;
import com.hitech.dms.web.service.activityclaim.activityClaimService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/activity/claim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimController.class);
	@Autowired
	private activityClaimService activityClaimService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	/**
	 * 
	 * @param authentication
	 * @param activityPlanId
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/getActivityDetails")
	public ResponseEntity<?> getActivityDetails(OAuth2Authentication authentication,
			@RequestParam(value = "activityPlanId") Integer activityPlanId, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityClaimService.getActivityDetails(userCode, device,
				activityPlanId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestedData
	 * @param files
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
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
			responseModel = activityClaimService.createActivityClaim(userCode, requestedData, files, device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Activity Claim Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Activity Claim Not Created!!");
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
	
	@GetMapping("/getActivityClaimDetailsById")
	public ResponseEntity<?> getActivityClaimDetailsById(OAuth2Authentication authentication,
			@RequestParam(value = "activityClaimId") Integer activityClaimId, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityClaimService.getActivityClaimDetailsById(userCode, device,
				activityClaimId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity calim Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping(value = "/checkActivityApprovalPermissions")
	public ResponseEntity<?> checkActivityApprovalPermissions(@RequestBody ActivityPermissionRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModel = activityClaimService.checkActivityPermissions(userCode, requestModel);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse
					.setDescription("Fetch Activity Approval Permissions Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Approval Permissions Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Approval Permissions Detail Not Fetched or server side error.");
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
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityClaimApproveResponse responseModel = activityClaimService.approveRejectActivityClaim(userCode,requestModel, device);
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
	@PostMapping("/fetchActivityPlanNoForClaim")
	public ResponseEntity<?> getActivityPlanNoAuto(
			@RequestBody ActivityRequestModel activityPlanNoAutoRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<ActivityResponseModel> planNoList = activityClaimService.activityPlanAuto(userCode,activityPlanNoAutoRequestModel);
		if (planNoList != null && !planNoList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan No. Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(planNoList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
