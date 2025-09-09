package com.hitech.dms.web.controller.paymentReceipt.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.paymentReceipt.view.PaymentReceiptViewDao;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptViewMainResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/paymentReceiptView")
@SecurityRequirement(name = "hitechApis")
public class PaymentReceiptViewRestController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentReceiptViewRestController.class);
	
	@Autowired
	private PaymentReceiptViewDao paymentReceiptViewDao; 
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchPaymentview")
	public ResponseEntity<?> fetchPaymentSearch(@RequestBody PaymentReceiptViewRequestModel paymentRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		
		PaymentReceiptViewMainResponse responseModel = paymentReceiptViewDao.fetchPaymentReceiptViewList(userCode,paymentRequestModel);

		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Payment View List on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment view List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
