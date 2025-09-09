/**
 * 
 */
package com.hitech.dms.web.controller.machinepo.update;

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
import com.hitech.dms.web.dao.machinepo.approval.MachinePoApprovalDao;
import com.hitech.dms.web.model.machinepo.approval.request.MachinePoApprovalRequestModel;
import com.hitech.dms.web.model.machinepo.approval.response.MachinePoApprovalResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/machinepo")
@SecurityRequirement(name = "hitechApis")
public class MachinePOApprovalController {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOApprovalController.class);

	@Autowired
	private MachinePoApprovalDao machinePoApprovalDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping("/approveRejectMachinePO")
	public ResponseEntity<?> approveRejectMachinePO(@RequestBody MachinePoApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		MachinePoApprovalResponseModel responseModel = machinePoApprovalDao.approveRejectMachinePO(userCode,
				requestModel, device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine PO Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Machine PO Approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/approveMachinePO")
	public ResponseEntity<?> approveMachinePO(@RequestBody MachinePoApprovalRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		MachinePoApprovalResponseModel responseModel = machinePoApprovalDao.approveRejectMachinePO(userCode,
				requestModel, device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine PO Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Machine PO Approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
