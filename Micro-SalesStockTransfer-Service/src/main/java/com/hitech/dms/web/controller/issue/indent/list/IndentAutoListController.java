/**
 * 
 */
package com.hitech.dms.web.controller.issue.indent.list;

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
import com.hitech.dms.web.dao.issue.indent.list.IndentAutoListDao;
import com.hitech.dms.web.model.issue.indent.list.request.IndentAutoListRequestModel;
import com.hitech.dms.web.model.issue.indent.list.response.IndentAutoListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/issue")
@SecurityRequirement(name = "hitechApis")
public class IndentAutoListController {
	private static final Logger logger = LoggerFactory.getLogger(IndentAutoListController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private IndentAutoListDao searchDao;

	@PostMapping("/fetchIndentListForIssue")
	public ResponseEntity<?> fetchIndentListForIssue(@RequestBody IndentAutoListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<IndentAutoListResponseModel> responseList = searchDao.fetchIndentListForIssue(userCode, requestModel);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Indent Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Indent Search Auto List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Indent Search Auto List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
