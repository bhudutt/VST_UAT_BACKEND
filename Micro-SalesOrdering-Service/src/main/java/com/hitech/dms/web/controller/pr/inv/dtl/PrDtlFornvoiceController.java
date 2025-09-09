/**
 * 
 */
package com.hitech.dms.web.controller.pr.inv.dtl;

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
import com.hitech.dms.web.dao.pr.inv.dtl.PrDtlFornvoiceDao;
import com.hitech.dms.web.model.pr.inv.dtl.request.PrDtlFornvoiceRequestModel;
import com.hitech.dms.web.model.pr.inv.dtl.response.PrDtlFornvoiceResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class PrDtlFornvoiceController {
	private static final Logger logger = LoggerFactory.getLogger(PrDtlFornvoiceController.class);

	@Autowired
	private PrDtlFornvoiceDao dtlFornvoiceDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchPurchaseReturnDtlForInv")
	public ResponseEntity<?> fetchPurchaseReturnDtlForInv(@RequestBody PrDtlFornvoiceRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PrDtlFornvoiceResponseModel responseModel = dtlFornvoiceDao.fetchPurchaseReturnDtlForInv(userCode,
				requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Purchase Return Detail For Invoice on " + formatter.format(new Date()));
			codeResponse.setMessage("Purchase Return List For Invoice Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Purchase Return Detail For Invoice Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
