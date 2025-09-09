/**
 * 
 */
package com.hitech.dms.web.controller.indent.view;

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
import com.hitech.dms.web.dao.indent.view.IndentViewDao;
import com.hitech.dms.web.model.indent.view.request.IndentViewRequestModel;
import com.hitech.dms.web.model.indent.view.response.IndentViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/indent")
@SecurityRequirement(name = "hitechApis")
public class IndentViewController {
	private static final Logger logger = LoggerFactory.getLogger(IndentViewController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private IndentViewDao dtlDao;

	@PostMapping("/view")
	public ResponseEntity<?> fetchIndentViewForIssue(@RequestBody IndentViewRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		IndentViewResponseModel response = dtlDao.fetchIndentView(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Indent Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Indent Detail Successfully fetched.");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Indent Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
}
