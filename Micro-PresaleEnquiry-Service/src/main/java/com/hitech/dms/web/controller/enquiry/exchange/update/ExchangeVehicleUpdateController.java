/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.exchange.update;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.enquiry.exchange.update.ExchangeVehicleUpdateDao;
import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleUpdateRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleUpdateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/oldVehicle")
@SecurityRequirement(name = "hitechApis")
public class ExchangeVehicleUpdateController {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeVehicleUpdateController.class);

	@Autowired
	private ExchangeVehicleUpdateDao exchangeVehicleUpdateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/update")
	public ResponseEntity<?> fetchExchangeVehicleEnquiryList(
			@RequestBody ExchangeVehicleUpdateRequestModel requestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ExchangeVehicleUpdateResponseModel responseModel = exchangeVehicleUpdateDao.updateExchangeVeh(userCode,
				requestModel);
		if (responseModel != null && responseModel.getMsg() != null
				&& responseModel.getStatusCode() == WebConstants.STATUS_CREATED_201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Old Vehicle Inventory Updated on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Vehicle Inventory Updated Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Vehicle Inventory Not Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
