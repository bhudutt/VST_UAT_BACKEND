/**
 * 
 */
package com.hitech.dms.web.controller.inv.cancel.approval;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.inv.cancel.approval.InvCancelApprovalDao;
import com.hitech.dms.web.model.inv.cancel.approval.request.InvCancelApprovalRequestModel;
import com.hitech.dms.web.model.inv.cancel.approval.response.InvCancelApprovalResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/invoice")
@SecurityRequirement(name = "hitechApis")
public class InvCancelApprovalController {
	private static final Logger logger = LoggerFactory.getLogger(InvCancelApprovalController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@Autowired
	private InvCancelApprovalDao approvalDao;

	@PostMapping("/approveRejectInvCancel")
	public ResponseEntity<?> approveRejectInvCancel(@RequestBody InvCancelApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		InvCancelApprovalResponseModel responseModel = approvalDao.approveRejectInvCancel(userCode, requestModel,
				device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("DC Cancel Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
			
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Invoice Cancel Approval Not Fetched or server side error.");
			
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
}
