/**
 * 
 */
package com.hitech.dms.web.controller.activityplan.dtl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.dao.activityplan.dtl.ActivityPlanDTLDao;
import com.hitech.dms.web.model.activityplan.dtl.request.ActivityPlanDTLRequestModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanDTLResponseModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanHDRResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activityPlan")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanDTLController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanDTLController.class);

	@Autowired
	private ActivityPlanDTLDao activityPlanDTLDao;

	@Autowired
	private ActivityCommonDao activityCommonDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchActivityPlanDTL")
	public ResponseEntity<?> fetchActivityPlanDTL(@RequestBody ActivityPlanDTLRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanDTLResponseModel responseModel = activityPlanDTLDao.fetchActivityPlanDTLList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getActivityPlanJsonArr().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Plan Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/fetchActivityPlanHDRDTL/{activityPlanHdrId}/{isFor}")
	public ResponseEntity<?> fetchActivityPlanHDRDTL(@PathVariable BigInteger activityPlanHdrId,
			@PathVariable String isFor, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanHDRResponseModel responseModel = activityCommonDao.fetchActivityPlanHDRDTL(userCode,
				activityPlanHdrId, isFor);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Plan Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
