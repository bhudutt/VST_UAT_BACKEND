/**
 * 
 */
package com.hitech.dms.web.controller.machinepo.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.controller.machinepo.calculation.MachinePOCalculationController;
import com.hitech.dms.web.dao.machinepo.search.MachinePOSearchDao;
import com.hitech.dms.web.model.machinepo.search.request.MachinePOSearchRequestModel;
import com.hitech.dms.web.model.machinepo.search.response.MachinePOSearchListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/machinepo")
@SecurityRequirement(name = "hitechApis")
public class MachinePOSearchController {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCalculationController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private MachinePOSearchDao machinePOSearchDao;

	@PostMapping("/fetchMachinePOSearchList")
	public ResponseEntity<?> fetchMachinePOSearchList(@RequestBody MachinePOSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MachinePOSearchListResponseModel responseModel = machinePOSearchDao.fetchMachinePOSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine PO Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine PO Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine PO Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
