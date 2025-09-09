/**
 * 
 */
package com.hitech.dms.web.controller.dealer.branch.assign.dtl;

import java.text.SimpleDateFormat;
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
import com.hitech.dms.web.dao.dealer.assign.branch.dtl.AssignUserToBranchDTLDao;
import com.hitech.dms.web.model.dealer.branchassign.view.request.AssignUserToBranchDTLRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.view.response.AssignUserToBranchDTLResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/dealer")
@SecurityRequirement(name = "hitechApis")
public class AssignUserToBranchDTLController {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchDTLController.class);

	@Autowired
	private AssignUserToBranchDTLDao assignUserToBranchDTLDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchAssignUserToBranchDTL")
	public ResponseEntity<?> fetchAssignUserToBranchDTL(@RequestBody AssignUserToBranchDTLRequestModel requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		AssignUserToBranchDTLResponseModel responseModel = null;
		try {
			responseModel = assignUserToBranchDTLDao.fetchAssignUserToBranchDTL(userCode, requestedData);
			if (responseModel != null && responseModel.getBranchList() != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Branch Assignment Detail Fetched on " + formatter.format(new Date()));
				codeResponse.setMessage("Branch Assignment Detail Fetched Successfully.");
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Branch To User Assignment Detail Not Fetched or server side error.");
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
