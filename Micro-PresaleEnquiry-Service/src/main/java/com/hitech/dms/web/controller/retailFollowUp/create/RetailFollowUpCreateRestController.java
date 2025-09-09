package com.hitech.dms.web.controller.retailFollowUp.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.retailFollowUp.RetailFollowUpCreateDao;
import com.hitech.dms.web.model.retailFollowUp.create.request.RetailFollowUpSubmitRequestModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpCreateResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpEnquirySelectionResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/retailFollowUp")
@SecurityRequirement(name = "hitechApis")
public class RetailFollowUpCreateRestController {
	private static final Logger logger = LoggerFactory.getLogger(RetailFollowUpCreateRestController.class);
	
	@Autowired
	private RetailFollowUpCreateDao retailFollowUpCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/fetchEnqHistoryPaymentDtl")
	public ResponseEntity<?> fetchEnqHistoryPaymentDtl(@RequestParam BigInteger enquiryId, OAuth2Authentication authentication, Device device) {
		
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName():null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		
		RetailFollowUpEnquirySelectionResponseModel responseModel = retailFollowUpCreateDao.fetchEnqDtlRetailHistory(userCode, enquiryId, device);

		if (responseModel.getEnqDtl() != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Retail FollowUp  on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Retail FollowUp  fetch Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Retail FollowUp  details Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
//	@GetMapping("/fetchRetailStagesList")
//	ResponseEntity<?> fetchRetailStagesList( @RequestParam BigInteger enquiryId,OAuth2Authentication authentication,Device device){
//		
//		String userCode = null;
//		userCode = authentication != null ? authentication.getUserAuthentication().getName():null;
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		BigInteger big = new BigInteger("26");
//		
//		Map<String, Object> response=  retailFollowUpCreateDao.fetchretailsStages(userCode, big, device);
//		
//		if (!response.isEmpty()) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch Retail Stage List  on " + formatter.format(new Date()));
//			codeResponse.setMessage("Retail Stage List fetch Successfully fetched");
//		} else {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Retail Stage List Not Fetched or server side error.");
//		}
//		
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(response);
//		return ResponseEntity.ok(userAuthResponse);
//		
//	}
	
	@PostMapping("/submitRetailFollowUp")
	public ResponseEntity<?> submitRetailFollowUp(@Valid @RequestBody RetailFollowUpSubmitRequestModel requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName():null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		RetailFollowUpCreateResponseModel responseModel = null;
		try {
			responseModel = retailFollowUpCreateDao.createRetailFollowUp(userCode,
					requestedData, device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Retail FollowUp Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Retail FollowUp  Not Create or server side error.");
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
