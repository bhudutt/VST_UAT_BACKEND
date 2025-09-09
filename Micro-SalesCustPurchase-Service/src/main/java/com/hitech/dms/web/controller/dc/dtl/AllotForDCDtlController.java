/**
 * 
 */
package com.hitech.dms.web.controller.dc.dtl;

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
import com.hitech.dms.web.dao.dc.dtl.AllotForDCDtlDao;
import com.hitech.dms.web.model.dc.dtl.request.AllotForDCDtlRequestModel;
import com.hitech.dms.web.model.dc.dtl.response.AllotForDCDtlResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/dc")
@SecurityRequirement(name = "hitechApis")
public class AllotForDCDtlController {
	private static final Logger logger = LoggerFactory.getLogger(AllotForDCDtlController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private AllotForDCDtlDao dcDtlDao;

	@PostMapping("/fetchAllotDtlForDc")
	public ResponseEntity<?> fetchAllotDtlForDc(@RequestBody AllotForDCDtlRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		AllotForDCDtlResponseModel responseModel = dcDtlDao.fetchAllotDtlForDc(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Allotment Detail For DC on " + formatter.format(new Date()));
			codeResponse.setMessage("Allotment Detail For DC Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Allotment Detail For DC Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
