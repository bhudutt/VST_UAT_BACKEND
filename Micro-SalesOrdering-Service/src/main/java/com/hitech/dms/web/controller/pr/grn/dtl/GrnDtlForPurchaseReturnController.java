/**
 * 
 */
package com.hitech.dms.web.controller.pr.grn.dtl;

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
import com.hitech.dms.web.dao.pr.grn.dtl.GrnDtlForPurchaseReturnDao;
import com.hitech.dms.web.model.pr.grn.dtl.request.GrnDtlForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.dtl.response.GrnDtlForPurchaseReturnResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class GrnDtlForPurchaseReturnController {
	private static final Logger logger = LoggerFactory.getLogger(GrnDtlForPurchaseReturnController.class);

	@Autowired
	private GrnDtlForPurchaseReturnDao grnDtlForPurchaseReturnDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchGrnDtlForPurchaseReturn")
	public ResponseEntity<?> fetchGrnDtlForPurchaseReturn(@RequestBody GrnDtlForPurchaseReturnRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		GrnDtlForPurchaseReturnResponseModel responseModel = grnDtlForPurchaseReturnDao
				.fetchGrnDtlForPurchaseReturn(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Grn Detail For Purchase Return on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Detail For Purchase Return Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Detail For Purchase Return Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
