/**
 * 
 */
package com.hitech.dms.web.controller.additionalCriteria;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.addtionalcriteria.AdditinalCriteriaDao;
import com.hitech.dms.web.model.scheme.additionalCriteria.request.AdditinalCriteriaRequestModel;
import com.hitech.dms.web.model.scheme.additionalCriteria.response.SchemeTypeOnchangeResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/scheme")
@SecurityRequirement(name = "hitechApis")
public class AdditinalCriteriaController {
	private static final Logger logger = LoggerFactory.getLogger(AdditinalCriteriaController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@Autowired
	private AdditinalCriteriaDao dao;

	@PostMapping(value = "/fetchSchemeTypeOnchangeDetail")
	public ResponseEntity<?> create(@RequestBody AdditinalCriteriaRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		SchemeTypeOnchangeResponseModel responseModel = dao.fetchSchemeTypeOnchangeDetail(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription(
					"Fetch Additinal Criteria On Change Of Scheme Type  on " + formatter.format(new Date()));
			codeResponse.setMessage("Additinal Criteria On Change Of Scheme Type Successfully fetched");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Record Not Found Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
		return ResponseEntity.ok(userAuthResponse);
	}
}
