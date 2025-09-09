package com.hitech.dms.web.controller.allotment.enq.dtl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.allotment.enq.dtl.MachineEnqDtlForAllotDao;
import com.hitech.dms.web.model.allot.enq.dtl.request.MachineEnqDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.enq.dtl.response.MachineEnqDtlForAllotResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/allot")
@SecurityRequirement(name = "hitechApis")
public class MachineEnqDtlForAllotController {
	private static final Logger logger = LoggerFactory.getLogger(MachineEnqDtlForAllotController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private MachineEnqDtlForAllotDao enqDtlForAllotDao;

	@PostMapping("/fetchEnqDtlForAllot")
	public ResponseEntity<?> fetchEnqDtlForAllot(@RequestBody MachineEnqDtlForAllotRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MachineEnqDtlForAllotResponseModel responseModel = enqDtlForAllotDao.fetchEnqDtlForAllot(userCode,
				requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Detail For Allotment on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail For Allotment Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Detail For Allotment Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
