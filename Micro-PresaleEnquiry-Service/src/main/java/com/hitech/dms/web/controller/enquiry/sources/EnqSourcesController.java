/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.sources;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.sources.EnqSourcesDao;
import com.hitech.dms.web.model.enquiry.sources.request.EnqSourcesRequestModel;
import com.hitech.dms.web.model.enquiry.sources.response.EnqSourcesResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/sources")
@SecurityRequirement(name = "hitechApis")
public class EnqSourcesController {
	private static final Logger logger = LoggerFactory.getLogger(EnqSourcesController.class);

	@Autowired
	private EnqSourcesDao enqSourcesDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchEnqSourcesList/{isApplicableFor}")
	public ResponseEntity<?> fetchEnqSourcesList(@PathVariable String isApplicableFor,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnqSourcesResponseModel> responseModelList = enqSourcesDao.fetchEnqSourcesList(userCode, isApplicableFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Source List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqSourcesList")
	public ResponseEntity<?> fetchEnqSourcesList(@RequestBody EnqSourcesRequestModel sourcesRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnqSourcesResponseModel> responseModelList = enqSourcesDao.fetchEnqSourcesList(userCode,
				sourcesRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Source List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
