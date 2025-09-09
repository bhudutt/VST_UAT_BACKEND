package com.hitech.dms.web.controller.admin.role.edit;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleCreateDaoImpl;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleTreeNode;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleUnit;
import com.hitech.dms.web.dao.admin.role.edit.AdminRoleEditDaoImpl;
import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/admin")
public class AdminRoleEditRestController {

	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleEditRestController.class);

	@Autowired
	private AdminRoleCreateDaoImpl adminRoleCreateDaoImpl;
	
	
	@Autowired
	private AdminRoleEditDaoImpl adminRoleEditDaoImpl;
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/updateAdminRole")
	public ResponseEntity<?> createAdminRole(@RequestBody AdminRoleFunctionRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		AdminRoleCreateResponseModel  responseList = null;
		if (requestModel.getRole().getRoleId() != null) {
			responseList = adminRoleCreateDaoImpl.createAdminRole(userCode, requestModel,device);
		}else {
			responseList = new AdminRoleCreateResponseModel();
			responseList.setMsg("Role Id is Null | Admin Role Not Updated");
		}

		if (responseList != null && responseList.getRoleId() !=null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Admin Role Updated on " + formatter.format(new Date()));
			codeResponse.setMessage("Admin Role Updated Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Admin Role Not Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value = "/getRolesByRoleId/{roleId}")
	public ResponseEntity<?> getRolesByRoleId(@PathVariable BigInteger roleId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<String, Object> responseList= adminRoleEditDaoImpl.getRolesByRoleId(roleId, device);

		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Role List on " + formatter.format(new Date()));
			codeResponse.setMessage("Role List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Role List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
