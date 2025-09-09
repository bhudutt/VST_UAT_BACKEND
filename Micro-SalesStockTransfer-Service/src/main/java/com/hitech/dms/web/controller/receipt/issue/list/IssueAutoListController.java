/**
 * 
 */
package com.hitech.dms.web.controller.receipt.issue.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.hitech.dms.web.dao.receipt.issue.list.IssueAutoListDao;
import com.hitech.dms.web.model.stock.receipt.issue.request.IssueAutoListRequestModel;
import com.hitech.dms.web.model.stock.receipt.issue.response.IssueAutoListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/receipt")
@SecurityRequirement(name = "hitechApis")
public class IssueAutoListController {
	private static final Logger logger = LoggerFactory.getLogger(IssueAutoListController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private IssueAutoListDao searchDao;

	@PostMapping("/fetchIssueListForIssue")
	public ResponseEntity<?> fetchIssueListForIssue(@RequestBody IssueAutoListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<IssueAutoListResponseModel> responseList = searchDao.fetchIssueListForIssue(userCode, requestModel);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Issue Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Issue Search Auto List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Issue Search Auto List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
