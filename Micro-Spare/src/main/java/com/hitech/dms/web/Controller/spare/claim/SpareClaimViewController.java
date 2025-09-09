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
import com.hitech.dms.web.model.spare.claim.request.AgreeOrDisagreeClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.service.spareClaim.SpareClaimService;

@RestController
@RequestMapping("/api/v1/spareClaim/view")
public class SpareClaimViewController {

	@Autowired
	private SpareClaimService spareClaimService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchGrnDetails")
	public ResponseEntity<?> fetchGrnDetails(@RequestParam() int grnHdrId, @RequestParam() String pageName,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnClaimResponse spareGrnClaimResponse = spareClaimService.fetchGrnDetails(grnHdrId, pageName);
		if (spareGrnClaimResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare claim List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare claim List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnClaimResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchClaimDetails")
	public ResponseEntity<?> fetchClaimDetails(@RequestParam() int claimHdrId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnClaimResponse spareGrnClaimResponse = spareClaimService.fetchClaimDetails(claimHdrId);
		if (spareGrnClaimResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare claim List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare claim List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnClaimResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchGrnPartDetails")
	public ResponseEntity<?> fetchGrnPartDetails(@RequestParam() int grnHdrId,
			@RequestParam(required = false) String pageOrClaimType, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<PartNumberDetailResponse> partNumberDetailResponseList = spareClaimService.fetchGrnPartDetails(grnHdrId,
				pageOrClaimType);
		if (partNumberDetailResponseList != null && !partNumberDetailResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchGrnClaimPartDetails")
	public ResponseEntity<?> fetchGrnClaimPartDetails(@RequestParam() int claimHdrId,
			@RequestParam(required = false) String pageOrClaimType, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartNumberDetailResponse> partNumberDetailResponseList = spareClaimService.fetchGrnClaimPartDetails(claimHdrId,
				pageOrClaimType);
		if (partNumberDetailResponseList != null && !partNumberDetailResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/updateSpareClaim")
	public ResponseEntity<?> updateSpareClaim(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SpareClaimUpdateRequest spareClaimUpdateRequest, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareClaimService.updateSpareClaim(userCode, spareClaimUpdateRequest);
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

	@PostMapping("/ReSubmitSpareClaim")
	public ResponseEntity<?> ReSubmitSpareClaim(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart(value = "spareClaimRequest") SpareClaimRequest spareClaimRequest,
			@RequestPart(required = false) MultipartFile file, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareClaimService.ReSubmitSpareClaim(userCode, spareClaimRequest, file);
		if (spareGrnResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare Claim updated on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare Claim updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

}
