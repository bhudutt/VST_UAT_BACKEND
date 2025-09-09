/**
 * 
 */
package com.hitech.dms.web.controller.dealer.branch.assign.edit;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.hitech.dms.web.controller.dealer.branch.assign.AssignUserToBranchController;
import com.hitech.dms.web.dao.dealer.assign.branch.edit.AssignUserToBranchEditDao;
import com.hitech.dms.web.model.dealer.branchassign.dtl.request.AssignUserToBranchFormModel;
import com.hitech.dms.web.model.dealer.branchassign.dtl.response.AssignUserToBranchResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/dealer")
@SecurityRequirement(name = "hitechApis")
public class AssignUserToBranchEditController {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchController.class);

	@Autowired
	private AssignUserToBranchEditDao assignUserToBranchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/editAssignBranchesToUser")
	public ResponseEntity<?> editAssignBranchesToUser(@Valid @RequestBody AssignUserToBranchFormModel requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		AssignUserToBranchResponseModel responseModel = null;
		try {
			responseModel = assignUserToBranchDao.editAssignBranchesToUser(userCode, requestedData);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Branch Assignment Edited on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Branch To User Assignment Not Edited or server side error.");
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
