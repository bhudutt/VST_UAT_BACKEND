/**
 * 
 */
package com.hitech.dms.web.controller.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.scheme.type.responseModel.SchemeTypeMstResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/scheme")
@SecurityRequirement(name = "hitechApis")
public class CommonController {
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@Autowired
	private CommonDao dao;

	@GetMapping(value = "/typeList")
	public ResponseEntity<?> create(@RequestParam String isIncludeInactive, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		List<SchemeTypeMstResponseModel> responseList = dao.fetchSchemeTypeMstList(userCode, isIncludeInactive);

		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Scheme Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Scheme Type List Successfully fetched");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseList);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Record Not Found Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseList);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
		return ResponseEntity.ok(userAuthResponse);
	}
}
