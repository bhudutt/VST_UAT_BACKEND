/**
 * 
 */
package com.hitech.dms.web.controller.approval.permission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.sales.permission.SalesPermissionDao;
import com.hitech.dms.web.model.sales.permission.request.SalesPermissionRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/sales")
@SecurityRequirement(name = "hitechApis")
public class SalesPermissionController {
	private static final Logger logger = LoggerFactory.getLogger(SalesPermissionController.class);
	
	@Autowired
	private SalesPermissionDao salesPermissionDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/checkSalesPermissions")
	public ResponseEntity<?> checkSalesPermissions(@RequestBody SalesPermissionRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModel = salesPermissionDao.checkSalesPermissions(userCode, requestModel);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse
					.setDescription("Fetch Sales Approval Permissions Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Sales Approval Permissions Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Sales Approval Permissions Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
