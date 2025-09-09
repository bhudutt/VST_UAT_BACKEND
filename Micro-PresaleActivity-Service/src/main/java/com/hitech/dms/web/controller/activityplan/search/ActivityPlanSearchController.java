/**
 * 
 */
package com.hitech.dms.web.controller.activityplan.search;

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
import com.hitech.dms.web.dao.activityplan.search.ActivityPlanSearchDao;
import com.hitech.dms.web.model.activityplan.search.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.search.response.ActivityPlanSearchResultResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activityPlan")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanSearchController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanSearchController.class);

	@Autowired
	private ActivityPlanSearchDao activityPlanSearchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchActivityPlanList")
	public ResponseEntity<?> fetchActivityPlanList(@RequestBody ActivityPlanSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanSearchResultResponseModel responseModel = activityPlanSearchDao.fetchActivityPlanList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Plan Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Data Not Found On This Date Range ");
	
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
