/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.controller.paymentReceipt.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.hitech.dms.web.dao.paymentReceipt.create.PaymentReceiptCreateDao;
import com.hitech.dms.web.entity.paymentReceipt.PaymentReceiptEntity;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.request.PaymentReceiptCreateRequestModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.EnquiryNoAutoSearchResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptCreateResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptModeResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptTypeResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/paymentReceipt")
@SecurityRequirement(name = "hitechApis")
public class PaymentRecepitRestController {

	@Autowired
	PaymentReceiptCreateDao paymentReceiptCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/getPaymentReceiptMode")
	public ResponseEntity<?> getPaymentReceiptMode(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<PaymentReceiptModeResponseModel> paymentModeList = paymentReceiptCreateDao.getPaymentReceiptMode(userCode);
		if (paymentModeList != null && !paymentModeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Payment Mode Receipt on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Mode Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(paymentModeList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getPaymentReceiptType")
	public ResponseEntity<?> getPaymentReceiptType(@RequestParam() BigInteger enquiryId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<PaymentReceiptTypeResponseModel> paymentTypeList = paymentReceiptCreateDao.getPaymentReceiptType(userCode,
				enquiryId);
		if (paymentTypeList != null && !paymentTypeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Payment Receipt Type on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Type Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Type Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(paymentTypeList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/submitPaymentReceipt")
	public ResponseEntity<?> submitPaymentReceipt(@Valid @RequestBody PaymentReceiptEntity requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		PaymentReceiptCreateResponseModel responseModel = paymentReceiptCreateDao.createPaymentRecepit(userCode,
				requestedData, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Payment Receipt Create on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt  Not Create or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getEnquryNoAutoSearch")
	public ResponseEntity<?> getEnquryNoAutoSearch(@RequestParam() String enquiryNo, @RequestParam() String branchId,
			@RequestParam(required = false) String isFor, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<EnquiryNoAutoSearchResponseModel> paymentTypeList = paymentReceiptCreateDao.enquryNoAutoSearch(userCode,
				enquiryNo, branchId, isFor);
		if (paymentTypeList != null && !paymentTypeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry No on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry No Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry No Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(paymentTypeList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getReceiptAmtTotalByEnqId")
	public ResponseEntity<?> getReceiptAmtTotalByEnqId(@RequestParam() BigInteger enquiryId,@RequestParam() BigInteger receiptTypeId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<PaymentReceiptTypeResponseModel> paymentTypeList = paymentReceiptCreateDao.getReceiptAmtTotalByEnqId(userCode,
				enquiryId,receiptTypeId);
		if (paymentTypeList != null && !paymentTypeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Payment Receipt Type on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Type Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Payment Receipt Type Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(paymentTypeList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
