/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.activity.ActivityPlanListDao;
import com.hitech.dms.web.model.enquiry.activity.request.ActivityPlanListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityPlanListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanListController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanListController.class);

	@Autowired
	private ActivityPlanListDao activityPlanListDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchEnqPlanActivityList/{pcId}/{activityId}/{dealerId}/{fieldActivityDate}")
	public ResponseEntity<?> fetchEnqPlanActivityList(@PathVariable Integer pcId, @PathVariable Integer activityId,
			@PathVariable Long dealerId, @PathVariable Date fieldActivityDate, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		List<ActivityPlanListResponseModel> responseModelList = activityPlanListDao.fetchEnqActivityPlanList(userCode,
				pcId, activityId, dealerId, fieldActivityDate);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Plan Activity List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqPlanActivityList")
	public ResponseEntity<?> fetchEnqPlanActivityList(@RequestBody ActivityPlanListRequestModel activityPlanListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanListResponseModel> responseModelList = activityPlanListDao.fetchEnqActivityPlanList(userCode,
				activityPlanListRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Plan Activity List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
