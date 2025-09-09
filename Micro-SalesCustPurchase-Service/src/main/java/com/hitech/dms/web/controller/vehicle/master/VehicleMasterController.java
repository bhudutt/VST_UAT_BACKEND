package com.hitech.dms.web.controller.vehicle.master;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.vehicle.master.VehicleMasterDao;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterDetailsModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterRequestModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterSearchListResultResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/master")
@SecurityRequirement(name = "hitechApis")
public class VehicleMasterController {
	
private static final Logger logger = LoggerFactory.getLogger(VehicleMasterController.class);

private SimpleDateFormat getSimpleDateFormat() {
	return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
}

@Autowired
private VehicleMasterDao vehicleMasterDao;


@PostMapping("/vehicleMasterDTL")
public ResponseEntity<?> vehicleMasterDTL(@RequestBody VehicleMasterRequestModel requestModel,
		OAuth2Authentication authentication) {
	String userCode = null;
	if (authentication != null) {
		userCode = authentication.getUserAuthentication().getName();
	}
	HeaderResponse userAuthResponse = new HeaderResponse();
	MessageCodeResponse codeResponse = new MessageCodeResponse();
	SimpleDateFormat formatter = getSimpleDateFormat();
	
	VehicleMasterDetailsModel responseModel = vehicleMasterDao.vehicleMasterDTL(userCode,requestModel);
	
	if (responseModel != null) {
		codeResponse.setCode("EC200");
		codeResponse.setDescription("Fetch Vehicle Master List on " + formatter.format(new Date()));
		codeResponse.setMessage("Vehicle Master List Successfully fetched");
	} else {
		codeResponse.setCode("EC200");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Vehicle Master List Not Fetched");
	}
	userAuthResponse.setResponseCode(codeResponse);
	userAuthResponse.setResponseData(responseModel);
	return ResponseEntity.ok(userAuthResponse);
}

@PostMapping(value = "/VehicleMasterSearchList")
public ResponseEntity<?> VehicleMasterSearchList(@RequestBody VehicleMasterRequestModel requestModel,
		OAuth2Authentication authentication, Device device, HttpServletRequest request) {
	String userCode = null;
	if (authentication != null) {
		userCode = authentication.getUserAuthentication().getName();
	}
	
	HeaderResponse userAuthResponse = new HeaderResponse();
	MessageCodeResponse codeResponse = new MessageCodeResponse();
	SimpleDateFormat formatter = getSimpleDateFormat();
	
	VehicleMasterSearchListResultResponseModel responseModel = vehicleMasterDao.VehicleMasterSearchList(userCode,
			requestModel);
	if (responseModel != null && responseModel.getSearchResult() != null
			&& !responseModel.getSearchResult().isEmpty()) {
		codeResponse.setCode("EC200");
		codeResponse.setDescription("Vehicle Master Search List on " + formatter.format(new Date()));
		codeResponse.setMessage("Vehicle Master Search Successfully fetched");
	} else {
		codeResponse.setCode("EC200");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Vehicle Master Search Not Fetched.");
	}
	userAuthResponse.setResponseCode(codeResponse);
	userAuthResponse.setResponseData(responseModel);
	return ResponseEntity.ok(userAuthResponse);
}


}
