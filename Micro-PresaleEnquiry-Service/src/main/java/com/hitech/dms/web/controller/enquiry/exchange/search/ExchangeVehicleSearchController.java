/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.exchange.search;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.exchange.search.ExchangeVehicleSearchDao;
import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleSearchRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeEnquiryRes;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleSearchMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/search")
@SecurityRequirement(name = "hitechApis")
public class ExchangeVehicleSearchController {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeVehicleSearchController.class);

	@Autowired
	private ExchangeVehicleSearchDao exchangeVehicleSearchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchExchangeVehicleEnquiryList")
	public ResponseEntity<?> fetchExchangeVehicleEnquiryList(
			@RequestBody ExchangeVehicleSearchRequestModel requestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ExchangeVehicleSearchMainResponseModel responseModel = exchangeVehicleSearchDao
				.fetchExchangeVehicleEnquiryList(userCode, requestModel);
		if (responseModel != null && responseModel.getExchangeVehicleSearchList() != null
				&& !responseModel.getExchangeVehicleSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Exchange Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Exchange Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Exchange Serach List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/getEnquiryNumbers")
	public ResponseEntity<?> getEnquiryNumbers(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ExchangeEnquiryRes> responseModelList = exchangeVehicleSearchDao.getEnquiryNumbers(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Pin List By City Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
