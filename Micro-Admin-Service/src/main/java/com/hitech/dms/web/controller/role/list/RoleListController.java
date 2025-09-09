/**
 * 
 */
package com.hitech.dms.web.controller.role.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.model.user.role.request.UserRoleRequestModel;
import com.hitech.dms.web.model.user.role.response.UserRoleResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/role")
@SecurityRequirement(name = "hitechApis")
public class RoleListController {
	private static final Logger logger = LoggerFactory.getLogger(RoleListController.class);

	@Autowired
	private CommonDao commonDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchUserRoleList")
	public ResponseEntity<?> fetchUserRoleList(@RequestBody UserRoleRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<UserRoleResponseModel> responseList = commonDao.fetchUserRoleList(userCode, requestModel.getIsFor(),
				requestModel.getUserId());
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch User Role Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("User Role Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Role Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/fetchRoleList/{isFor}")
	public ResponseEntity<?> fetchRoleList(@PathVariable String isFor, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<UserRoleResponseModel> responseList = commonDao.fetchUserRoleList(userCode, isFor, null);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Role Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Role Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Role Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
