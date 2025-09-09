package com.hitech.dms.web.controller.customer.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.customer.CustomerCreDao;
import com.hitech.dms.web.model.customer.create.CustServiceRep;
import com.hitech.dms.web.model.customer.create.CustServiceSaveReq;
import com.hitech.dms.web.model.customer.create.CustomerDetailsResponse;
import com.hitech.dms.web.model.customer.create.CustomerMobileRes;
import com.hitech.dms.web.model.customer.create.LookupResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "hitechApis")
public class CustomerCreateRestController {
	
	@Autowired
	private CustomerCreDao customerCredao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchCustomerDTLByCustMob")
	public ResponseEntity<?> fetchCustomerDTLByCustMob(@RequestParam(name="customerId") BigInteger customerId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CustomerDetailsResponse responseModelList = customerCredao.fetchCustomerDTLByCustMob(userCode,customerId);
		if (responseModelList != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Mobile on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Mobile Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Mobile Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}	
	
	@GetMapping("/fetchCustomerMobNameDtl")
	public ResponseEntity<?> fetchCustomerMobNameDtl(@RequestParam(name = "searchText") String searchText,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CustomerMobileRes> responseModelList = customerCredao.fetchCustomerMobNameDtl(userCode,searchText);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Mobile Name on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Mobile Name Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Mobile Name Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchCustomerMaster")
	public ResponseEntity<?> fetchCustomerMaster(@RequestParam(name="lookupTypeCode") String lookupTypeCode, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();

		List<LookupResponse> responseList = customerCredao.fetchCustomerMaster(lookupTypeCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer master List on " + LocalDate.now());
			codeResponse.setMessage("Customer master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Customer master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?> createCustomerDetail(@RequestBody CustServiceSaveReq requestModel,BindingResult bindingResult,@RequestHeader("Authorization") String authorizationHeader,OAuth2Authentication authentication,Device device,HttpServletRequest request) {

	    String userCode = (authentication != null) ? authentication.getUserAuthentication().getName() : null;
	    HeaderResponse userAuthResponse = new HeaderResponse();
	    MessageCodeResponse codeResponse = new MessageCodeResponse();

	    if (bindingResult.hasErrors()) {
			userAuthResponse.setResponseCode(CommonUtils.getCodeResponse("customer service not created or server side error."));
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));
	    }

	    CustServiceRep responseModel = customerCredao.createCustomerDetail(authorizationHeader, userCode, requestModel, device);

	    boolean isSuccess = responseModel != null && WebConstants.STATUS_OK_200==responseModel.getStatusCode();

	    codeResponse.setCode(isSuccess ? "EC200" : "EC500");
	    codeResponse.setDescription((isSuccess ? "Successful" : "Unsuccessful") + " on " + LocalDate.now());
	    codeResponse.setMessage(isSuccess ? responseModel.getMsg() : "Error While Creating Spare Customer Order.");

	    userAuthResponse.setResponseCode(codeResponse);
	    userAuthResponse.setResponseData(responseModel);

	    return ResponseEntity.ok(userAuthResponse);
	}
	
	

	@GetMapping("/alreadyExistMobileNo")
	public ResponseEntity<?> alreadyExistMobileNo(@RequestParam(name="mobileNo") String mobileNo, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();

		BigInteger available = customerCredao.alreadyExistMobileNo(mobileNo);
		if (available != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Mobile No. already exist " + LocalDate.now());
			codeResponse.setMessage("Mobile No. already exist");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Mobile No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(available);
		return ResponseEntity.ok(userAuthResponse);
	}

	
}
