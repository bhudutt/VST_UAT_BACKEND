package com.hitech.dms.web.controller.paymentReceipt.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.hitech.dms.web.dao.paymentReceipt.search.PaymentReceiptSearchDao;
import com.hitech.dms.web.model.paymentReceipt.search.request.PaymentReceiptSearchRequestModel;
import com.hitech.dms.web.model.paymentReceipt.search.response.PaymentReceiptSearchMainResponseModel;
import com.hitech.dms.web.model.paymentReceipt.search.response.PaymentReceiptSearchResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */


@RestController
@RequestMapping("/paymentReceiptSearch")
@SecurityRequirement(name = "hitechApis")
public class PaymentReceiptSearchRestController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentReceiptSearchRestController.class);
	
	@Autowired
	private PaymentReceiptSearchDao paymentReceiptSearchDao;
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	@PostMapping("/fetchPaymentSearch")
	public ResponseEntity<?> fetchPaymentSearch(@RequestBody PaymentReceiptSearchRequestModel paymentRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PaymentReceiptSearchMainResponseModel responseModel = paymentReceiptSearchDao.fetchPaymentReceiptList(userCode,
				paymentRequestModel);
		if (responseModel != null && !responseModel.getPaymentSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Payment Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
