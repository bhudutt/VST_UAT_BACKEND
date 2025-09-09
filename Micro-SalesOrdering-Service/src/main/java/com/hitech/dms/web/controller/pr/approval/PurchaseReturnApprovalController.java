/**
 * 
 */
package com.hitech.dms.web.controller.pr.approval;

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
import com.hitech.dms.web.dao.pr.approval.PurchaseReturnApprovalDao;
import com.hitech.dms.web.model.pr.approval.request.PurchaseReturnApprovalRequestModel;
import com.hitech.dms.web.model.pr.approval.response.PurchaseReturnApprovalResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class PurchaseReturnApprovalController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnApprovalController.class);

	@Autowired
	private PurchaseReturnApprovalDao purchaseReturnApprovalDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping("/approveRejectPurchaseReturn")
	public ResponseEntity<?> approveRejectPurchaseReturn(@RequestBody PurchaseReturnApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		PurchaseReturnApprovalResponseModel responseModel = purchaseReturnApprovalDao
				.approveRejectPurchaseReturn(userCode, requestModel, device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine Purchase Return Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Machine Purchase Return Approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
