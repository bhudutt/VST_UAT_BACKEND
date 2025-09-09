package com.hitech.dms.web.controller.productTrial.search;

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
import com.hitech.dms.web.dao.enquiry.productTrial.search.ProductTrialSearchDao;
import com.hitech.dms.web.model.productTrial.search.request.ProductTrialSearchRequestModel;
import com.hitech.dms.web.model.productTrial.search.response.ProductTrialSearchMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class ProductTrialSearchRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductTrialSearchRestController.class);
	
	@Autowired
	private ProductTrialSearchDao productTrialSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchProductTrialSearchList")
	public ResponseEntity<?> fetchActivityClaimSearch(@RequestBody ProductTrialSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ProductTrialSearchMainResponseModel responseModel = productTrialSearchDao.fetchProductTrialSearchList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getProductTrailSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Product Trail Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Trail  List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Trail  Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

}
