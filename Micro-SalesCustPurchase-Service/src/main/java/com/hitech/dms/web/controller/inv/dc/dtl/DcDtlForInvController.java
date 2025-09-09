/**
 * 
 */
package com.hitech.dms.web.controller.inv.dc.dtl;

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
import com.hitech.dms.web.dao.inv.dc.dtl.DcDtlForInvDao;
import com.hitech.dms.web.model.inv.dc.dtl.request.DcDtlForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.dtl.response.DcDtlForInvResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/invoice")
@SecurityRequirement(name = "hitechApis")
public class DcDtlForInvController {
	private static final Logger logger = LoggerFactory.getLogger(DcDtlForInvController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private DcDtlForInvDao dcDtlForInvDao;
	
	@PostMapping("/fetchDcDetailForInvoice")
	public ResponseEntity<?> fetchDcDetailForInvoice(@RequestBody DcDtlForInvRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DcDtlForInvResponseModel responseModel = dcDtlForInvDao.fetchDcDetailForInvoice(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DC Detail For Invoice on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Detail For Invoice Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Detail For Invoice Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
