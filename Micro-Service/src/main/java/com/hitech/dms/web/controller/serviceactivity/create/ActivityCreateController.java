package com.hitech.dms.web.controller.serviceactivity.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.AppConstants;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.create.ActivityCreateDao;
import com.hitech.dms.web.entity.activity.create.request.ActivityRequestEntity;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestDtlModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewAfterSubmitResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewResponseModel;
import com.hitech.dms.web.model.activity.create.response.ServiceActivitySearchListResultResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class ActivityCreateController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityCreateController.class);

	@Autowired
	private ActivityCreateDao activityCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchActivityPlanNoAuto")
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
		List<ActivityResponseModel> planNoList = activityCreateDao.activityPlanAuto(userCode,activityPlanNoAutoRequestModel);
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
	
	
	
	@GetMapping("/fetchJobcard")
	public ResponseEntity<?>fetchJobcard(
			@RequestParam (value="searchText") String searchText ,
			@RequestParam (value="planNoId") String planNoId ,
			OAuth2Authentication authentication){
		
		String userCode = null;
		
		if(authentication !=null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
		Map<String, Object> responseModelList = activityCreateDao.getJobCardNumberList(userCode,
				searchText,planNoId);
		System.out.println("responseModelList"+responseModelList);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("JobCard  List on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	@GetMapping("/fetchActivityPlanDetailsById/{activityPlanHdrId}")
	public ResponseEntity<?> fetchActivityPlanDetailsById(
			@PathVariable Integer activityPlanHdrId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		ActivityResponseModel planNoList = activityCreateDao.fetchActivityPlanDetailsById(userCode,activityPlanHdrId);
		if (planNoList != null) {
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
	
	@GetMapping("/fetchJobcardDetailsByActivityId/{activityNameId}")
	public ResponseEntity<?> fetchJobcardDetailsByActivityId(
			@PathVariable BigInteger activityNameId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<ActivityRequestDtlModel> planNoList =activityCreateDao.fetchJobcardDetailsByActivityId(userCode,activityNameId);
		if (planNoList != null) {
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
	
	@PostMapping(value = "/createServiceActivity")
	public ResponseEntity<?> createServiceActivity(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ActivityRequestEntity requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Quotation not created or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = activityCreateDao.createServiceActivity(authorizationHeader, userCode, requestModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Service Quotation GRN.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/viewSubmitActivity")
	public ResponseEntity<?> viewSubmitActivity(
			@RequestBody ActivityRequestModel activityPlanNoAutoRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		ServiceActivitySearchListResultResponse planNoList = activityCreateDao.viewSubmitActivity(userCode,activityPlanNoAutoRequestModel);
		if (planNoList != null && planNoList.getSearchResult() != null
				&& !planNoList.getSearchResult().isEmpty()) {
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
	
	@GetMapping("/fetchActivityServiceViewList")
	public ResponseEntity<?> fetchActivityServiceViewList(@RequestParam("activityHdrId") Integer activityHdrId,
			OAuth2Authentication authentication,Device device,HttpServletRequest request)
			{
		String userCode=null;
		if(authentication !=null) {
			userCode=authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse=new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityViewAfterSubmitResponseModel> responseModelList = activityCreateDao.fetchActivityServiceViewList(userCode,device,activityHdrId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Plan View List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Plan View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Plan View Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
