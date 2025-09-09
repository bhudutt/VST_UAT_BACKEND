package com.hitech.dms.web.controller.productTrial.create;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.enquiry.productTrial.ProductTrialCreateDao;
import com.hitech.dms.web.entity.productTrial.ProductTrialHdrEntity;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialCreateResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


/**
 * @author vinay.gautam
 *
 */
@RestController
@RequestMapping("/productTrialCreate")
@SecurityRequirement(name = "hitechApis")
public class ProductTrialCreateRestController {
	private static final Logger logger = LoggerFactory.getLogger(ProductTrialCreateRestController.class);
	
	@Autowired
	private ProductTrialCreateDao productTrialCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchEnqProspectDtl")
	public ResponseEntity<?> fetchEnqProspectDtl(@RequestBody PaymentReceiptViewRequestModel requestModel,
			OAuth2Authentication authentication) {
		
		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
		userCode = authentication != null ? authentication.getUserAuthentication().getName():null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		
		ProductTrialEnqProspectHistoryResponse responseModel = productTrialCreateDao.fetchEnqProspectEnqHistory(userCode,requestModel);

		if (responseModel.getEnqProspect() != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry and Prospect  on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry and Prospect fetch Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry and Prospect details Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/submitProductTrial")
	public ResponseEntity<?> submitProductTrial(@Valid @RequestBody ProductTrialHdrEntity requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		ProductTrialCreateResponseModel responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		try {
			responseModel = productTrialCreateDao.createProductTrial(userCode,
					requestedData, device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Product Trial Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Product Trial  Not Create or server side error.");
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
