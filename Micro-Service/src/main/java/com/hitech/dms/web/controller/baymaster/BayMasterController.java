package com.hitech.dms.web.controller.baymaster;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.baymaster.create.BayMasterDao;
import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.baymaster.responselist.BayMasterResponseModel;
import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;
import com.hitech.dms.web.model.baymaster.create.request.BayMasterRequest;
import com.hitech.dms.web.model.baymaster.create.request.BayMasterRequestModel;
import com.hitech.dms.web.model.delayreason.create.request.DelayReasonRequestName;
import com.hitech.dms.web.model.service.booking.ServiceBookingResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/baymaster")
@SecurityRequirement(name = "hitechApis")
public class BayMasterController {

	
	@Autowired 
	BayMasterDao bayMasterDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createBayMaster(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody BayMasterRequestModel bayMasterRequestList, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BayMasterResponse bayMasterResponse = bayMasterDao.createBayMaster(userCode, bayMasterRequestList.getBayMasterRequestList()
				);
		if (bayMasterResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bay Master added on " + formatter.format(new Date()));
			codeResponse.setMessage(bayMasterResponse.getMsg());
		} else if (bayMasterResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Master Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(bayMasterResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/update")
	public ResponseEntity<?> updateBayMaster(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer id, @RequestParam() String isActive, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BayMasterResponse bayMasterResponse = bayMasterDao.updateBayMaster(userCode, id, isActive
				);
		if (bayMasterResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bay Master Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(bayMasterResponse.getMsg());
		} else if (bayMasterResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Master Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(bayMasterResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchBayMasterList")
	public ResponseEntity<?> fetchBayMasterList(@RequestParam () Integer dealerId,
			@RequestParam() Integer page, @RequestParam() Integer size,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<BayMasterResponseModel> bayMasterResponseList = bayMasterDao.fetchBayMasterList(userCode, dealerId, page, size);
		if (bayMasterResponseList != null && !bayMasterResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bay Master List on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(bayMasterResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchBayMasterType")
	public ResponseEntity<?> fetchBayTypeList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String bayType = "BAY_TYPE";
		List<BayTypeModel> bayTypeModelList = bayMasterDao.fetchBayTypeList(bayType);
		if (bayTypeModelList != null && !bayTypeModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bay Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bay Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(bayTypeModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
