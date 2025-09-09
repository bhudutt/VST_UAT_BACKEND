package com.hitech.dms.web.controller.oldchassis.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.oldchassis.search.OldChassisSearchDao;
import com.hitech.dms.web.model.oldchassis.search.request.OldChassisSearchListRequestModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisSearchListResultResponseModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisValidateEVRListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/oldChassis")
@SecurityRequirement(name = "hitechApis")
public class OldChassisSearchListController {

	private static final Logger logger = LoggerFactory.getLogger(OldChassisSearchListController.class);	
	@Autowired
	private OldChassisSearchDao oldchassisSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/OldChassisSearchList")
	public ResponseEntity<?> OldChassisSearchList(@RequestBody OldChassisSearchListRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		OldChassisSearchListResultResponseModel responseModel = oldchassisSearchDao.OldChassisSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchResult() != null
				&& !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Old Chassis Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchValidateEVRList")
	public ResponseEntity<?> fetchModelList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisValidateEVRListResponseModel> responseModelList = oldchassisSearchDao.fetchValidateEVRList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
