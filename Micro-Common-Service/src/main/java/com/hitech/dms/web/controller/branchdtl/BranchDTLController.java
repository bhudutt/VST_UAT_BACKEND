/**
 * 
 */
package com.hitech.dms.web.controller.branchdtl;

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
import com.hitech.dms.web.dao.branch.BranchDao;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/branch")
@SecurityRequirement(name = "hitechApis")
public class BranchDTLController {
	private static final Logger logger = LoggerFactory.getLogger(BranchDTLController.class);

	@Autowired
	private BranchDao branchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchBranchListByDealerId")
	public ResponseEntity<?> fetchBranchListByDealerId(@RequestBody BranchDTLRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<BranchDTLResponseModel> responseModel = branchDao.fetchBranchListByDealerId(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Branch Detail List  on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(" Branch Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchMainBranchDtlByDealerId")
	public ResponseEntity<?> fetchMainBranchDtlByDealerId(@RequestBody BranchDTLRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchDTLResponseModel responseModel = branchDao.fetchMainBranchDtlByDealerId(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Main Branch Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Main Branch Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Main Branch Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchBranchDtlByBranchId")
	public ResponseEntity<?> fetchBranchDtlByBranchId(@RequestBody BranchDTLRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchDTLResponseModel responseModel = branchDao.fetchBranchDtlByBranchId(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Branch Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
