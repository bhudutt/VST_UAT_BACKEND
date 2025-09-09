package com.hitech.dms.web.controller.productTrial.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.productTrial.view.ProductTrialViewDao;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class ProductTrialViewRestController {
	
	@Autowired
	private ProductTrialViewDao productTrialViewDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchProductTrailView")
	public ResponseEntity<?> fetchEnqProspectDtl(@RequestBody PaymentReceiptViewRequestModel requestModel,
			OAuth2Authentication authentication) {
		
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName():null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		
		ProductTrialEnqProspectHistoryResponse responseModel = productTrialViewDao.fetchProductTrailView(userCode,requestModel);

		if (responseModel.getEnqProspect() != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Product Trail View on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Trail View fetch Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Trail View Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	

}
