/**
 * 
 */
package com.hitech.dms.web.controller.opex.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.opex.view.OpexBudgetViewDao;
import com.hitech.dms.web.model.opex.view.request.OpexBudgetViewRequestModel;
import com.hitech.dms.web.model.opex.view.response.OpexBudgetViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/opex")
@SecurityRequirement(name = "hitechApis")
public class OpexBudgetViewController {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetViewController.class);
	
	@Autowired
	private OpexBudgetViewDao dao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/view")
	public ResponseEntity<?> view(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody OpexBudgetViewRequestModel requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		OpexBudgetViewResponseModel responseModel = dao.fetchOpexBudgetView(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Opex Budget Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Opex Budget Detail Successfully fetched");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Opex Budget Detail Not Found Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
		return ResponseEntity.ok(userAuthResponse);
	}
}
