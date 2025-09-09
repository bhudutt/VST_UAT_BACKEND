package com.hitech.dms.web.Controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.common.PartyLedgerRequest;
import com.hitech.dms.web.model.common.PinCodeDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceResponse;
import com.hitech.dms.web.service.common.CommonService;

@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

	@Autowired
	private CommonService commonService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchPartNumberDetailsBinWise")
	public ResponseEntity<?> fetchPartNumberDetails(@RequestParam(required = false) Integer partId, 
			@RequestParam() Integer branchId,
			@RequestParam(required = false) Integer poHdrId, 
			@RequestParam(required = false) Integer coId, 
			@RequestParam(required = false) Integer dcId, 
			@RequestParam(required = false) Integer pickListId,
			@RequestParam() Integer refDocId,
			@RequestParam() String flag,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartNumberDetails> partNumberDetailsList = commonService.fetchPartNumberDetails(partId, userCode, branchId, poHdrId,
				coId, dcId, pickListId, refDocId, flag);
		if (partNumberDetailsList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Number Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailsList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/fetchBasicPartPrice")
	public ResponseEntity<?> fetchPartsBasicUnitsPrice(@RequestBody SpareInvoicePriceRequest req,
		OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareInvoicePriceResponse partNumberDetailsList = commonService.getPartUnitPrice(req, userCode);
		if (partNumberDetailsList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Price found successfully " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Price Not fetched successfully or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailsList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	@GetMapping("/searchPinCode")
	public ResponseEntity<?> searchPinCode(@RequestParam() String searchText,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> list = commonService.searchPinCode(searchText);
		if (list != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Pin Code Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin Code Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(list);
		return ResponseEntity.ok(userAuthResponse);
	}	


	@GetMapping("/fetchPinCodeDetails")
	public ResponseEntity<?> fetchPinCodeDetails(@RequestParam() Integer pinId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PinCodeDetails pinCodeDetails = commonService.fetchPinCodeDetails(pinId);
		if (pinCodeDetails != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Pin Code Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin Code Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(pinCodeDetails);
		return ResponseEntity.ok(userAuthResponse);
	}

}
