/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.view;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.enquiry.view.EnquiryViewDao;
import com.hitech.dms.web.model.enquiry.view.request.EnquiryViewRequestModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class EnquiryViewController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryViewController.class);

	@Autowired
	private EnquiryViewDao enquiryViewDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchEnqDTL/{enquiryId}")
	public ResponseEntity<?> fetchEnqSourcesList(@PathVariable BigInteger enquiryId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryViewMainResponseModel responseModelList = enquiryViewDao.fetchEnqDTL(userCode, enquiryId, 0);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry DTL on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqDTL")
	public ResponseEntity<?> fetchEnqSourcesList(@RequestBody EnquiryViewRequestModel enquiryViewRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryViewMainResponseModel responseModelList = enquiryViewDao.fetchEnqDTL(userCode, enquiryViewRequestModel);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry DTL on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
