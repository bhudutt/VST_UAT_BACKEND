/**
 * 
 */
package com.hitech.dms.web.controller.grn.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.grn.search.dao.GrnSearchDao;
import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseMainModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/grn")
@SecurityRequirement(name = "hitechApis")
public class GrnSearchController {
	private static final Logger logger = LoggerFactory.getLogger(GrnSearchController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private GrnSearchDao grnSearchDao;

	@PostMapping("/salesGrnSearch")
	public ResponseEntity<?> salesGrnSearch(@RequestBody GrnSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		GrnSearchResponseMainModel responseModel = grnSearchDao.salesGrnSearch(userCode, requestModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Grn Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Grn Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Grn Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
