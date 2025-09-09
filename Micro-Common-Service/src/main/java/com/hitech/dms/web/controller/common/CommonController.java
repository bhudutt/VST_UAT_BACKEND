/**
 * 
 */
package com.hitech.dms.web.controller.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.CommonSearchHierarchy;
import com.hitech.dms.web.model.common.response.CommonHoDetailResponse;
import com.hitech.dms.web.model.common.response.ServerDateTimeModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/utility")
public class CommonController {
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private Environment environment;
	
	
	@Autowired
	CommonSearchHierarchy dao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/status/check")
	public String status() {
		return "Working on port : " + environment.getProperty("server.port") + " with = "
				+ environment.getProperty("security.oauth2.resource.token-info-uri");
	}

	@GetMapping("/serverDateTime")
	public ResponseEntity<?> serverDateTime(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServerDateTimeModel responseModel = new ServerDateTimeModel();
		if (responseModel != null && responseModel.getServerDate() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Server Date & Time on " + formatter.format(new Date()));
			codeResponse.setMessage("Server Date & Time  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Server Date & Time Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getCommonHoDetail")
	public ResponseEntity<?> fetchCommonHoDetail(@RequestParam(required = false) Integer flag,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller "+flag +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CommonHoDetailResponse responseModel = dao.fetchCommonHoOrgDetail(flag, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch HO User Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("HO User Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("HO User Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
}
