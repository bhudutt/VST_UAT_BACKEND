/**
 * 
 */
package com.hitech.dms.web.controller.dealer.branch.assign.search;

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
import com.hitech.dms.web.dao.dealer.assign.branch.search.AssignUserToBranchSearchDao;
import com.hitech.dms.web.model.dealer.branchassign.search.request.AssignUserToBranchSearchRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.search.response.AssignUserToBranchSearchMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/dealer")
@SecurityRequirement(name = "hitechApis")
public class AssignUserToBranchSearchController {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchSearchController.class);

	@Autowired
	private AssignUserToBranchSearchDao assignUserToBranchSearchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/searchAssignedUserBranchList")
	public ResponseEntity<?> searchAssignedUserBranchList(
			@RequestBody AssignUserToBranchSearchRequestModel requestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		AssignUserToBranchSearchMainResponseModel responseModel = assignUserToBranchSearchDao
				.searchAssignedUserBranchList(userCode, requestModel);
		if (responseModel != null && !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Employee Assignment List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Assignment List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Assignment List Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
