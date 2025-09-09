package com.hitech.dms.web.controller.activityplan.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activityplan.ActivityPlanDao;
import com.hitech.dms.web.model.activity.create.request.ActivityPlanApprovalRequestModel;
import com.hitech.dms.web.model.activitymaster.response.ActivityNumberSearchResponseModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanEditRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanApprovalResponse;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanSearchResultResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanServiceTypeListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateDealerWiseListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStatusResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/activityPlan")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanCreateRestController {

	@Autowired 
	ActivityPlanDao activityPlanDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createActivityPlan(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ActivityPlanRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanResponseModel responseModel=activityPlanDao.createActivityPlan(userCode, device,requestModel);
		
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Plan added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchStateList")
	public ResponseEntity<?> fetchStateList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanStateListResponseModel> responseModelList = activityPlanDao.fetchStateList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchStateDealerWiseList")
	public ResponseEntity<?> fetchStateDealerList(@RequestParam("stateId") Integer stateId,
			@RequestParam("dealerCode") String dealerCode,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanStateDealerWiseListResponseModel> responseModelList = activityPlanDao.fetchStateDealerWiseList(userCode,device,stateId,dealerCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Dealer Wise List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Dealer Wise List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Dealer Wise Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchServiceActivityTypeList")
	public ResponseEntity<?> fetchServiceActivityTypeList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanServiceTypeListResponseModel> responseModelList = activityPlanDao.fetchServiceActivityTypeList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchActivityPlanSearchList")
	public ResponseEntity<?> fetchActivityPlanSearchList(@RequestBody ActivityPlanSearchRequestModel requestModel,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanSearchResultResponseModel responseModelList = activityPlanDao.fetchActivityPlanSearchList(userCode,device,requestModel);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Plan Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch AActivity Plan Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Plan Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchActivityPlanViewList")
	public ResponseEntity<?> fetchActivityPlanViewList(@RequestParam("activityId") Integer activityId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanViewResponseModel> responseModelList = activityPlanDao.fetchActivityPlanViewList(userCode,device,activityId);
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
	
	@GetMapping("/fetchActivityPlanStatusList")
	public ResponseEntity<?> fetchActivityPlanStatusList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanStatusResponseModel> responseModelList = activityPlanDao.fetchActivityPlanStatusList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchActivityPlanStatusListId")
	public ResponseEntity<?> fetchActivityPlanStatusListId(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityPlanStatusResponseModel> responseModelList = activityPlanDao.fetchActivityPlanStatusListId(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service Type Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/ActivityPlanStatusUpdate")
	public ResponseEntity<?> ActivityPlanStatusUpdate(@RequestBody
			ActivityPlanApprovalRequestModel requestModel,  OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanResponseModel responseModel=activityPlanDao.ActivityPlanStatusUpdate(userCode, device,requestModel);

		if (responseModel.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Plan Status Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchActivityPlanNumberBySearchList")
	public ResponseEntity<?> fetchActivityPlanSearchList(@RequestParam("activityPlanNo") String activityPlanNo,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityNumberSearchResponseModel> responseModelList = activityPlanDao.fetchActivityPlanNumberBySearchList(userCode,device,activityPlanNo);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Plan Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Plan Number Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Plan Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/approveRejectActivityPlan")
	public ResponseEntity<?> approveRejectActivityPlan(@RequestBody com.hitech.dms.web.model.activityplan.request.ActivityPlanApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanApprovalResponse responseModel = activityPlanDao.approveRejectActivityPlan(userCode,
				requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Plan Approval Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Plan Approval Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/ActivityPlanEdit")
   	public ResponseEntity<?> ActivityPlanEdit(@RequestBody ActivityPlanEditRequestModel requestModel, BindingResult bindingResult,
   			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
   			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		ActivityPlanResponseModel responseModel = null;
   		System.out.println("requestModel::::::::::::::Controller" + requestModel);
   		if (bindingResult.hasErrors()) {
   			codeResponse.setCode("EC400");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Activity Plan not Updated or server side error.");

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
   		responseModel = activityPlanDao.ActivityPlanEdit(authorizationHeader, userCode, requestModel, device);
   		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
   			codeResponse.setMessage(responseModel.getMsg());
   		} else {
   			codeResponse.setCode("EC500");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Error While Update Activity Plan.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModel);
   		return ResponseEntity.ok(userAuthResponse);
   	}
	
	
}
