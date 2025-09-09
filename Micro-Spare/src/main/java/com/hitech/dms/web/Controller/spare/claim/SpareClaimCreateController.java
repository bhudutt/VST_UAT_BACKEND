package com.hitech.dms.web.Controller.spare.claim;

import java.math.BigInteger;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.service.spareClaim.SpareClaimService;

@RestController
@RequestMapping("/api/v1/spareClaim/create")
public class SpareClaimCreateController {

	@Autowired
	private SpareClaimService spareClaimService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/fetchClaimType")
	public ResponseEntity<?> fetchClaimType(OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> claimTypeList = spareClaimService.fetchClaimType();
		if (claimTypeList != null && !claimTypeList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Claim Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Claim Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Claim Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(claimTypeList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/searchGrnNumberForClaimType")
	public ResponseEntity<?> searchGrnOrInvoiceNumber(@RequestParam() String searchType,
			@RequestParam() String searchText, @RequestParam() String claimType,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> grnNumberList = spareClaimService
				.searchGrnNumberForClaimType(searchType, searchText, claimType, userCode);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/createSpareClaim")
	public ResponseEntity<?> createSpareClaim(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
//			@RequestBody SpareClaimRequest spareClaimRequest, 
			@RequestPart(value="spareClaimRequest") SpareClaimRequest spareClaimRequest,
			@RequestPart(required = false) MultipartFile file,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareClaimService.createSpareClaim(userCode, spareClaimRequest, file);
		if (spareGrnResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare Claim added on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare Claim Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
