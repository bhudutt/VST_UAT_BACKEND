package com.hitech.dms.web.controller.jobcard.billing.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingPLORequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingSearchRequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobCardBillingRequestModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCommonViewResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCreateResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCustomerTypeListResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingNumberSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingPLOResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingSearchResultResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingDetailsCommonResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSaleTypeResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardNumberSearchResponseModel;
import com.hitech.dms.web.service.jobcard.billing.JobCardBillingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/jobcardBilling")
@SecurityRequirement(name = "hitechApis")
public class JobCardBillingCreateRestController {
	
    @Autowired
	private JobCardBillingService jobCardBillingService;
    
    private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
    
    @PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestBody JobCardBillingRequestModel requestModel, BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobBillingCreateResponseModel responseModel = null;
		System.out.println("requestModel::::::::::::::Controller" + requestModel);
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card Billing not created or server side error.");

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
		responseModel = jobCardBillingService.create(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Job Card Billing.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    @PostMapping(value = "/jobCardBillingUpdate")
   	public ResponseEntity<?> jobCardBillingUpdate(@RequestBody JobCardBillingRequestModel requestModel, BindingResult bindingResult,
   			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
   			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		JobBillingCreateResponseModel responseModel = null;
   		System.out.println("requestModel::::::::::::::Controller" + requestModel);
   		if (bindingResult.hasErrors()) {
   			codeResponse.setCode("EC400");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Job Card Billing not created or server side error.");

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
   		responseModel = jobCardBillingService.jobCardBillingUpdate(authorizationHeader, userCode, requestModel, device);
   		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
   			codeResponse.setMessage(responseModel.getMsg());
   		} else {
   			codeResponse.setCode("EC500");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Error While Creating Job Card Billing.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModel);
   		return ResponseEntity.ok(userAuthResponse);
   	}

    
    @GetMapping(value ="/getSaleTypeList")
	public ResponseEntity<?> getSaleTypeList(@RequestHeader(value="Authorization",required=true)String authorizationHeader,
			OAuth2Authentication authentication,Device device, HttpServletRequest request){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<JobCardBillingSaleTypeResponseModel> responseModelList = jobCardBillingService.getSaleTypeList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Sale Type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Sale Type List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Sale Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    @GetMapping(value ="/fetchJobCardBillingList")
   	public ResponseEntity<?> fetchJobCardBillingList(@RequestParam("jobCardNo") String jobCardNo, @RequestHeader(value="Authorization",required=true)String authorizationHeader,
   			OAuth2Authentication authentication,Device device, HttpServletRequest request){
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		List<JobCardBillingSearchResponseModel> responseModelList = jobCardBillingService.fetchJobCardBillingList(authorizationHeader, userCode,jobCardNo);
   		if (responseModelList != null && !responseModelList.isEmpty()) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Job Card List " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Card List Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Job Card List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    @GetMapping(value ="/fetchJobCardBillingDetailsList")
   	public ResponseEntity<?> fetchJobCardBillingDetailsList(@RequestParam("roId") Integer roId, @RequestParam("flag") Integer flag, @RequestHeader(value="Authorization",required=true)String authorizationHeader,
   			OAuth2Authentication authentication,Device device, HttpServletRequest request){
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		JobCardBillingDetailsCommonResponseModel responseModelList = jobCardBillingService.fetchJobCardBillingDetailsList(authorizationHeader, userCode,roId,flag);
   		if (responseModelList != null) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Job Card Details List " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Card Details List Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Job Card Details List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    @PostMapping(value ="/fetchJobBillingSearchList")
   	public ResponseEntity<?> fetchJobBillingSearchList(@RequestBody JobBillingSearchRequestModel requestModel,  @RequestHeader(value="Authorization",required=true)String authorizationHeader,
   			OAuth2Authentication authentication,Device device, HttpServletRequest request){
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		JobBillingSearchResultResponseModel responseModelList = jobCardBillingService.fetchJobBillingSearchList(authorizationHeader, userCode,requestModel);
   		if (responseModelList != null ) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Job Card List " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Search List Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Search List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    @GetMapping(value ="/fetchJobBillingViewList")
   	public ResponseEntity<?> fetchJobBillingViewList(@RequestParam("roBillingId") Integer roBillingId,@RequestParam("flag") Integer flag, @RequestHeader(value="Authorization",required=true)String authorizationHeader,
   			OAuth2Authentication authentication,Device device, HttpServletRequest request){
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		JobBillingCommonViewResponseModel responseModelList = jobCardBillingService.fetchJobBillingViewList(authorizationHeader, userCode,roBillingId,flag);
   		if (responseModelList != null ) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Job Billing View List " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Billing View List Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Job Billing View List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    

    @PostMapping(value ="/fetchJobBillingPartLabourOutsideLBRList")
   	public ResponseEntity<?> fetchJobBillingPartLabourOutsideLBRList(@RequestBody JobBillingPLORequestModel requestModel,  @RequestHeader(value="Authorization",required=true)String authorizationHeader,
   			OAuth2Authentication authentication,Device device, HttpServletRequest request){
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		JobBillingPLOResponseModel responseModelList = jobCardBillingService.fetchJobBillingPartLabourOutsideLBRList(authorizationHeader, userCode,requestModel);
   		if (responseModelList != null ) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Job Card List " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job Part percentage  List Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Job percentage List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    //JobBillingNumberSearchResponseModel
    
    @GetMapping(value ="/fetchJobBillingNumberBySearchList")
	public ResponseEntity<?> fetchJobBillingNumberBySearchList(@RequestParam("billingNumber") String billingNumber,
			@RequestHeader(value="Authorization",required=true)String authorizationHeader,
			OAuth2Authentication authentication,Device device, HttpServletRequest request){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<JobBillingNumberSearchResponseModel> responseModelList = jobCardBillingService.fetchJobBillingNumberBySearchList(authorizationHeader, userCode,billingNumber);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Billing No List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Billing No List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Sale Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    @GetMapping(value ="/fetchJobCardNumberBySearchList")
	public ResponseEntity<?> fetchJobCardNumberBySearchList(@RequestParam("jobCardNumber") String jobCardNumber,
			@RequestHeader(value="Authorization",required=true)String authorizationHeader,
			OAuth2Authentication authentication,Device device, HttpServletRequest request){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<JobCardNumberSearchResponseModel> responseModelList = jobCardBillingService.fetchJobCardNumberBySearchList(authorizationHeader, userCode,jobCardNumber);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job Card Number List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Job Card Number List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card Number List Not Fetched or Job Card Number Not Found.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    @GetMapping(value ="/fetchCustomerTypeList")
	public ResponseEntity<?> fetchCustomerTypeList(
			@RequestHeader(value="Authorization",required=true)String authorizationHeader,
			OAuth2Authentication authentication,Device device, HttpServletRequest request){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<JobBillingCustomerTypeListResponseModel> responseModelList = jobCardBillingService.fetchCustomerTypeList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Customer Type Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Type Not Fetched or Customer Type Not Found.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
}
