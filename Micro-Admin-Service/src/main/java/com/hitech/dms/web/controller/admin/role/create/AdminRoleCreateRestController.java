package com.hitech.dms.web.controller.admin.role.create;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
import com.hitech.dms.web.dao.admin.role.create.AdminRoleCreateDaoImpl;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleTreeNode;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleUnit;
import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleCreateRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "hitechApis")
public class AdminRoleCreateRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleCreateRestController.class);

	@Autowired
	private AdminRoleCreateDaoImpl adminRoleCreateDaoImpl;
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	
	@PostMapping(value = "/getAssignedFunctionalityToRole")
	public ResponseEntity<?> getAssignedFunctionality(@RequestBody AdminRoleCreateRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Collection<AdminRoleTreeNode<AdminRoleUnit>>  responseList = adminRoleCreateDaoImpl.getAssignedFunctionalityToRole(requestModel,device);
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
	
	
	@PostMapping(value = "/createRole")
	public ResponseEntity<?> createAdminRole(@RequestBody AdminRoleFunctionRequestModel requestModel, 
	        OAuth2Authentication authentication, Device device, HttpServletRequest request) {
	    String userCode = null;
	    if (authentication != null) {
	        userCode = authentication.getUserAuthentication().getName();
	    }
	    
	    HeaderResponse userAuthResponse = new HeaderResponse();
	    MessageCodeResponse codeResponse = new MessageCodeResponse();
	    SimpleDateFormat formatter = getSimpleDateFormat();
	    
	    AdminRoleCreateResponseModel responseList = adminRoleCreateDaoImpl.createAdminRole(userCode, requestModel, device);
	    
	    if (responseList != null && responseList.getRoleId() != null) {
	        codeResponse.setCode("EC200");
	        codeResponse.setDescription("Admin Role processed on " + formatter.format(new Date()));
	        codeResponse.setMessage(responseList.getMsg());
	    } else {
	        codeResponse.setCode("EC500");
	        codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
	        codeResponse.setMessage("Admin Role processing failed or server side error.");
	    }
	    
	    userAuthResponse.setResponseCode(codeResponse);
	    userAuthResponse.setResponseData(responseList);
	    return ResponseEntity.ok(userAuthResponse);
	}
	
	

}
