/**
 * 
 */
package com.hitech.dms.web.controller.pr.inv.view;

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
import com.hitech.dms.web.dao.pr.inv.view.PrForInvoiceViewDao;
import com.hitech.dms.web.model.pr.inv.view.request.PrForInvoiceViewRequestModel;
import com.hitech.dms.web.model.pr.inv.view.response.PrForInvoiceViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class PrForInvoiceViewController {
	private static final Logger logger = LoggerFactory.getLogger(PrForInvoiceViewController.class);

	@Autowired
	private PrForInvoiceViewDao invoiceViewDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchPurchaseReturnInvDtl")
	public ResponseEntity<?> fetchPurchaseReturnInvDtl(@RequestBody PrForInvoiceViewRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PrForInvoiceViewResponseModel responseModel = invoiceViewDao.fetchPurchaseReturnInvDtl(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Purchase Return Invoice Detail View on " + formatter.format(new Date()));
			codeResponse.setMessage("Purchase Return Invoice Detail View Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Purchase Return Invoice Detail View Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
