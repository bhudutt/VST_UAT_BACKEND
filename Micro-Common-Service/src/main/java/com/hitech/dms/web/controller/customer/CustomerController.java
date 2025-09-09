/**
 * 
 */
package com.hitech.dms.web.controller.customer;

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
import com.hitech.dms.web.dao.customer.CustomerDao;
import com.hitech.dms.web.model.customer.request.CustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.customer.request.CustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByMobileResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/customer")
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerDao customerDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchCustomerDTLByMobileNo/{mobileNumber}/{isFor}/{dealerID}")
	public ResponseEntity<?> fetchCustomerDTLByMobileNo(@PathVariable String mobileNumber, @PathVariable String isFor,
			@PathVariable Long dealerID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CustomerDTLByMobileResponseModel> responseModelList = customerDao.fetchCustomerDTLByMobileNo(userCode,
				mobileNumber, isFor, dealerID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Mobile No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchCustomerDTLByMobileNo")
	public ResponseEntity<?> fetchCustomerDTLByMobileNo(
			@RequestBody CustomerDTLByMobileRequestModel customerDTLByMobileRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			customerDTLByMobileRequestModel.setUserCode(userCode);
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CustomerDTLByMobileResponseModel> responseModelList = customerDao
				.fetchCustomerDTLByMobileNo(customerDTLByMobileRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Mobile No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchCustomerDTLByCustID/{customerID}")
	public ResponseEntity<?> fetchCustomerDTLByCustID(@PathVariable Long customerID,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CustomerDTLByCustIDResponseModel> responseModelList = customerDao.fetchCustomerDTLByCustID(userCode,
				customerID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchCustomerDTLByCustID")
	public ResponseEntity<?> fetchCustomerDTLByCustID(
			@RequestBody CustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CustomerDTLByCustIDResponseModel> responseModelList = customerDao.fetchCustomerDTLByCustID(userCode,
				customerDTLByCustIDRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
