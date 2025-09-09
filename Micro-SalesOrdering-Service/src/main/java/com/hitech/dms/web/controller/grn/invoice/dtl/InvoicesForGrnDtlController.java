package com.hitech.dms.web.controller.grn.invoice.dtl;

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
import com.hitech.dms.web.dao.grn.invoice.dtl.InvoicesForGrnDtlDao;
import com.hitech.dms.web.model.grn.invoice.dtl.request.InvoicesForGrnDtlRequestModel;
import com.hitech.dms.web.model.grn.invoice.dtl.response.InvoicesForGrnDtlResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/grn")
@SecurityRequirement(name = "hitechApis")
public class InvoicesForGrnDtlController {
	private static final Logger logger = LoggerFactory.getLogger(InvoicesForGrnDtlController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private InvoicesForGrnDtlDao invoicesForGrnDtlDao;

	@PostMapping("/fetchInvoiceDtlForGrn")
	public ResponseEntity<?> fetchInvoiceDtlForGrn(@RequestBody InvoicesForGrnDtlRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		InvoicesForGrnDtlResponseModel responseModel = invoicesForGrnDtlDao.fetchInvoiceDtlForGrn(userCode,
				requestModel);
		if (requestModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Invoice Detail For Grn on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Detail For Grn Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Detail For Grn Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
