/**
 * 
 */
package com.hitech.dms.web.controller.dc.cancel.approval;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.dc.cancel.approval.DcCancelApprovalDao;
import com.hitech.dms.web.model.dc.cancel.approval.request.DcCancelApprovalRequestModel;
import com.hitech.dms.web.model.dc.cancel.approval.response.DcCancelApprovalResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/dc")
@SecurityRequirement(name = "hitechApis")
public class DcCancelApprovalController {
	private static final Logger logger = LoggerFactory.getLogger(DcCancelApprovalController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@Autowired
	private DcCancelApprovalDao approvalDao;

	@PostMapping("/approveRejectDcCancel")
	public ResponseEntity<?> approveRejectDcCancel(@RequestBody DcCancelApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		DcCancelApprovalResponseModel responseModel = approvalDao.approveRejectDcCancel(userCode, requestModel,
				device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("DC Cancel Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch DC Cancel Approval Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
