package com.hitech.dms.web.Controller.spare.branchTransfer.indent;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.service.branchSpareTransfer.indent.BranchSpareTransferIndentService;
import com.hitech.dms.web.service.branchSpareTransfer.indent.BranchSpareTransferIndentServiceImpl;
import com.hitech.dms.web.service.spareClaim.SpareClaimService;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;

@RestController
@RequestMapping("/api/v1/branchSpareTransferIndent")
public class CreateBranchSpareTransferController {

	
	@Autowired
	private BranchSpareTransferIndentService branchSpareTransferIndentService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchPartNumber")
	public ResponseEntity<?> searchPartNumber(@RequestParam() String searchText,
			@RequestParam() BigInteger branchId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> grnNumberList = branchSpareTransferIndentService
				.searchPartNumber(searchText, userCode);
		if (grnNumberList != null && !grnNumberList.isEmpty()) { 
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number List Successfully fetched");
		}else if(grnNumberList.isEmpty()){
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Part Number List on " + formatter.format(new Date()));
				codeResponse.setMessage("Part Stock Not Available Under This Dealer.");
		}else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchPartNumberDetails")
	public ResponseEntity<?> fetchPartNumberDetails(@RequestParam() Integer partId,
			@RequestParam() Integer branchId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartNumberDetails partNumberDetails = 
				branchSpareTransferIndentService.fetchPartNumberDetails(partId, userCode, branchId);
		if (partNumberDetails != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Number Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetails);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody IndentHdrEntity indentHdrEntity,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchSpareTransferResponse branchSpareTransferResponse = 
				branchSpareTransferIndentService.createBranchSpareTransferIndent(indentHdrEntity, userCode);
		if (branchSpareTransferResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Branch Spare Transfer Indent on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Spare Transfer Indent Successfully Created");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Spare Transfer Indent Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(branchSpareTransferResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
