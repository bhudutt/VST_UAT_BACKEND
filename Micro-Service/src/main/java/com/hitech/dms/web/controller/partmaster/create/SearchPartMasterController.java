package com.hitech.dms.web.controller.partmaster.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.partmaster.create.PartMasterCreateDao;
import com.hitech.dms.web.dao.partmaster.create.PartMasterDaoCreate;
import com.hitech.dms.web.model.partmaster.create.request.PartMasterFormRequestModel;
import com.hitech.dms.web.model.partmaster.create.request.PartSearchRequestModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterAutoListResponseModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/sparepartmastersearch")
@SecurityRequirement(name = "hitechApis")
public class SearchPartMasterController {

private static final Logger logger = LoggerFactory.getLogger(SearchPartMasterController.class);
	
	@Autowired
	private PartMasterCreateDao partMasterCreateDao;
	
	@Autowired PartMasterDaoCreate dao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@PostMapping("/fetchPartNumberList")
	public ResponseEntity<?> fetchPartNumberList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody PartSearchRequestModel partSearchRequestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartMasterAutoListResponseModel> responseModelList = partMasterCreateDao.fetchPartNumber(userCode,partSearchRequestModel);
		System.out.println("response at controller "+responseModelList);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value = "/partDetailsByPartNumber1")
	public ResponseEntity<?> getGrnDetailsByChassisNo(@RequestParam("partNumber") String partNumber,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PartMasterFormRequestModel responseModel = partMasterCreateDao.fetchPartNumberDetails(userCode,partNumber);
		System.out.println("response at controller"+responseModel);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
	
	
	@GetMapping(value = "partDetailsByPartNumber")
	public ResponseEntity<?> getPartDetailByPartNo(@RequestParam("partNumber") String partNumber,
			@RequestParam("isFor") String isFor,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PartyMappingResponseModel responseModel=dao.fetchPartNumberDetailsnew(userCode,partNumber,isFor);
		//PartMasterFormRequestModel responseModel = partMasterCreateDao.fetchPartNumberDetails(userCode,partNumber);
		System.out.println("response at controller"+responseModel);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
	
	
}
