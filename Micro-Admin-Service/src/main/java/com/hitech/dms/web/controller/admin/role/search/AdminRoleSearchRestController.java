package com.hitech.dms.web.controller.admin.role.search;

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
import com.hitech.dms.web.dao.admin.role.search.AdminRoleSearchDaoImpl;
import com.hitech.dms.web.dao.dealer.employee.search.DealerEmployeeSearchDaoImpl;
import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleCreateRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;
import com.hitech.dms.web.model.admin.role.search.response.AdminRoleSearchMainResponse;
import com.hitech.dms.web.model.admin.role.search.resquest.AdminRoleSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchMainResponse;


/**
 * @author vinay.gautam
 *
 */
@RestController
@RequestMapping("/admin")
public class AdminRoleSearchRestController {
	


	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleSearchRestController.class);
	
	
	@Autowired
	private AdminRoleSearchDaoImpl adminRoleSearchDaoImpl;
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	
	@PostMapping("/searchRole")
	public ResponseEntity<?> fetchDealerEmployeeSearch(@RequestBody AdminRoleSearchRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		AdminRoleSearchMainResponse responseModel = adminRoleSearchDaoImpl.fetchAdminRoleSearch(userCode, requestModel);
		if (responseModel != null && !responseModel.getSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Admin role Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Admin role Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Admin role Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
