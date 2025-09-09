/**
 * 
 */
package com.hitech.dms.web.controller.issue.search;

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
import com.hitech.dms.web.dao.issue.search.IssueSearchDao;
import com.hitech.dms.web.model.issue.search.request.IssueSearchRequestModel;
import com.hitech.dms.web.model.issue.search.response.IssueSearchMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/issue")
@SecurityRequirement(name = "hitechApis")
public class IssueSearchController {
	private static final Logger logger = LoggerFactory.getLogger(IssueSearchController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private IssueSearchDao searchDao;

	@PostMapping("/search")
	public ResponseEntity<?> searchIssueList(@RequestBody IssueSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		IssueSearchMainResponseModel responseModel = searchDao.searchIssueList(userCode, requestModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Issue Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Issue Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Issue Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
