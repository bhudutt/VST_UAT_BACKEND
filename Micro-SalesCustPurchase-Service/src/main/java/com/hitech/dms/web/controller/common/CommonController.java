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
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.allot.deallot.response.MachineDeAllotResponseModel;
import com.hitech.dms.web.model.dc.common.response.CancelReasonResponseModel;
import com.hitech.dms.web.model.dc.common.response.DcTypeResponModel;
import com.hitech.dms.web.model.inv.common.response.InvoiceTypeResponseModel;

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

	@GetMapping(value = "fetchDcTypeList")
	public ResponseEntity<?> fetchDcTypeList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DcTypeResponModel> responseModelList = commonDao.fetchDcTypeList(userCode);

		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DC Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "fetchInvoiceTypeList")
	public ResponseEntity<?> fetchInvoiceTypeList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<InvoiceTypeResponseModel> responseModelList = commonDao.fetchInvoiceTypeList(userCode);

		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Invoice Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "fetchCancelReasonList/{code}")
	public ResponseEntity<?> fetchCancelReasonList(@PathVariable(name = "code") String code,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CancelReasonResponseModel> responseModelList = commonDao.fetchCancelReasonList(userCode, code);

		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DC Cancel Reason List on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Cancel Reason List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("DC Cancel Reason List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/*
	 * @GetMapping(value = "updateCreationDate/{id}/{date}/{table}") public
	 * ResponseEntity<?> updateCreationDate(@PathVariable(name = "id") String id,
	 * 
	 * @PathVariable(name = "date") String date,@PathVariable(name = "table") String
	 * table, OAuth2Authentication authentication, Device device, HttpServletRequest
	 * request) { String userCode = null; if (authentication != null) { userCode =
	 * authentication.getUserAuthentication().getName(); } HeaderResponse
	 * userAuthResponse = new HeaderResponse(); MessageCodeResponse codeResponse =
	 * new MessageCodeResponse(); SimpleDateFormat formatter =
	 * getSimpleDateFormat(); MachineDeAllotResponseModel responseModelList =
	 * commonDao.updateCreationDate(userCode, id,date,table);
	 * 
	 * if (responseModelList != null ) { codeResponse.setCode("EC200");
	 * codeResponse.setDescription("Update " + formatter.format(new Date()));
	 * codeResponse.setMessage("Update Successfully"); } else {
	 * codeResponse.setCode("EC200"); codeResponse.setDescription("Unsuccessful on "
	 * + formatter.format(new Date()));
	 * codeResponse.setMessage("Update some Issue or server side error."); }
	 * userAuthResponse.setResponseCode(codeResponse);
	 * userAuthResponse.setResponseData(responseModelList); return
	 * ResponseEntity.ok(userAuthResponse); }
	 */

}
