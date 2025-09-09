/**
 * 
 */
package com.hitech.dms.web.controller.pr.grn.autosearch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.hitech.dms.web.dao.pr.grn.autosearch.GrnsForPurchaseReturnDao;
import com.hitech.dms.web.model.pr.grn.autosearch.request.GrnsForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.autosearch.response.GrnsForPurchaseReturnResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class GrnsForPurchaseReturnController {
	private static final Logger logger = LoggerFactory.getLogger(GrnsForPurchaseReturnController.class);

	@Autowired
	private GrnsForPurchaseReturnDao grnsForPurchaseReturnDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchGrnListForPurchaseReturn")
	public ResponseEntity<?> fetchGrnListForPurchaseReturn(@RequestBody GrnsForPurchaseReturnRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GrnsForPurchaseReturnResponseModel> responseModelList = grnsForPurchaseReturnDao
				.fetchGrnListForPurchaseReturn(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Grn List For Purchase Return on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn List For Purchase Return Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn List For Purchase Return Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
