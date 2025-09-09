/**
 * 
 */
package com.hitech.dms.web.controller.inv.dc.list;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.inv.dc.list.DcDtlForCustInvDao;
import com.hitech.dms.web.model.inv.dc.list.request.DcListForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcDtlForCustInvResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/invoice")
@SecurityRequirement(name = "hitechApis")
public class DcDtlForCustInvController {
	private static final Logger logger = LoggerFactory.getLogger(DcDtlForCustInvController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private DcDtlForCustInvDao custInvDao;

	@PostMapping("/fetchInvoiceDCDtlForCustomer")
	public ResponseEntity<?> fetchInvoiceDCDtlForCustomer(@RequestBody DcListForInvRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DcDtlForCustInvResponseModel responseModel = custInvDao.fetchInvoiceDCDtlForCustomer(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DC Detail For Invoice on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Detail For Invoice Successfully fetched");
			
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
			
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Detail For Invoice Not Fetched ");
			
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
}
