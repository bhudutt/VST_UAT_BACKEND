/**
 * 
 */
package com.hitech.dms.web.controller.opex.template.submit;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.opex.template.submit.OpexBudgetSubmitDao;
import com.hitech.dms.web.model.opex.template.submit.request.OpexBudgetSubmitRequestModel;
import com.hitech.dms.web.model.opex.template.submit.response.OpexBudgetSubmitResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/opex")
@SecurityRequirement(name = "hitechApis")
public class OpexBudgetSubmitController {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetSubmitController.class);

	@Autowired
	private OpexBudgetSubmitDao dao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody @Valid OpexBudgetSubmitRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		OpexBudgetSubmitResponseModel responseModel = dao.submitOpexBudget(authorizationHeader, userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Opex Budget submitted on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Opex Budget Not Submitted Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
}
