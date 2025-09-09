/**
 * 
 */
package com.hitech.dms.web.controller.machinepo.calculation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.machinepo.calculation.MachinePOCalculationDao;
import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOItemAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOTotalAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOItemAmntResponseModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOTotalAmntResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/calculate")
@SecurityRequirement(name = "hitechApis")
public class MachinePOCalculationController {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCalculationController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private MachinePOCalculationDao machinePOCalculationDao;

	@PostMapping("/calculateMachineItemAmount")
	public ResponseEntity<?> calculateMachineItemAmount(@RequestBody MachinePOItemAmntRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<MachinePOItemAmntResponseModel> responseModelList = machinePOCalculationDao
				.calculateMachineItemAmount(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Geo List By Pin Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item Amount Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item Amount Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/calculateMachinePOTotalAmount")
	public ResponseEntity<?> calculateMachinePOTotalAmount(@RequestBody MachinePOTotalAmntRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<MachinePOTotalAmntResponseModel> responseModelList = machinePOCalculationDao
				.calculateMachinePOTotalAmount(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Geo List By Pin Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Total PO Amount Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Total PO Amount Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
