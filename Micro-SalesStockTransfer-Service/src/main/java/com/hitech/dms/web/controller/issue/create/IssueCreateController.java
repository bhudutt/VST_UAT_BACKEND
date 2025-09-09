/**
 * 
 */
package com.hitech.dms.web.controller.issue.create;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.issue.create.IssueCreateDao;
import com.hitech.dms.web.model.issue.create.request.IssueCreateRequestModel;
import com.hitech.dms.web.model.issue.create.response.IssueCreateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/issue")
@SecurityRequirement(name = "hitechApis")
public class IssueCreateController {
	private static final Logger logger = LoggerFactory.getLogger(IssueCreateController.class);

	@Autowired
	private IssueCreateDao createDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody @Valid IssueCreateRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		IssueCreateResponseModel responseModel = createDao.createIssue(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Issue created on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Issue Not Created Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
}
