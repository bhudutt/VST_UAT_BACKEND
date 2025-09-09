/**
 * 
 */
package com.hitech.dms.web.controller.common;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.model.department.list.response.DepartmentListResponseModel;
import com.hitech.dms.web.model.desig.list.response.DealerDesiginationResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationLevelResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/common")
@SecurityRequirement(name = "hitechApis")
public class CommonController {
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonDao commonDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@GetMapping(value = "/fetchDepartmentList/{isActive}")
	public ResponseEntity<?> fetchDepartmentList(@PathVariable String isActive,
			@RequestParam(required = false) String applicableForDealer, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DepartmentListResponseModel> responseList = commonDao.fetchDepartmentList(userCode, applicableForDealer,
				isActive);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Department Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Department Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Department Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/fetchDesigList/{departmentId}")
	public ResponseEntity<?> fetchDesigList(@PathVariable Integer departmentId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DesiginationResponseModel> responseList = commonDao.fetchDesigList(userCode, departmentId);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Desigination Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Desigination Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Desigination Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/fetchDesigLevelList/{departmentId}")
	public ResponseEntity<?> fetchDesigLevelList(@PathVariable Integer departmentId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DesiginationLevelResponseModel> responseList = commonDao.fetchDesigLevelList(userCode, departmentId);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DesiginationLevel Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("DesiginationLevel Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("DesiginationLevel Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/fetchDealerDesigList/{departmentId}")
	public ResponseEntity<?> fetchDealerDesigList(@PathVariable Integer departmentId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerDesiginationResponseModel> responseList = commonDao.fetchDealerDesigList(userCode, departmentId);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Desigination Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Desigination Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Desigination Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/getPartyIdForCompanyCode/{dealerCode}")
	public ResponseEntity<?> getPartyIdForCamponyCode(@PathVariable Integer dealerCode, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		 String result = commonDao.getPartyIdFromdealercode(userCode,dealerCode);
		if (result != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PartyIdForCamponyCode on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyIdForCamponyCode List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyIdForCamponyCode List Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(result);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
}
