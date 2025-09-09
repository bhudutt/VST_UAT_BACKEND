package com.hitech.dms.web.Controller.spare.branchTransfer.receipt;

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
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.service.branchSpareTransfer.indent.BranchSpareTransferIndentServiceImpl;
import com.hitech.dms.web.service.branchSpareTransfer.issue.BranchSpareTransferIssueService;
import com.hitech.dms.web.service.branchSpareTransfer.receipt.BranchSpareTransferReceiptService;
import com.hitech.dms.web.service.spareClaim.SpareClaimService;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.request.BranchSpareTransferReceiptRequest;

@RestController
@RequestMapping("/api/v1/branchSpareTransferReceipt")
public class CreateBranchSpareTransferReceiptController {

	
	@Autowired
	private BranchSpareTransferReceiptService branchSpareTransferReceiptService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchIssueNumber")
	public ResponseEntity<?> searchIssueNumber(@RequestParam() String searchText, Integer branchId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> grnNumberList = branchSpareTransferReceiptService
				.searchIssueNumber(searchText, userCode);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Indent Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getBranchStoreList")
	public ResponseEntity<?> fetchBranchStoreList(@RequestParam() int indentToBranchId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> spareGrnFromResponseList = 
				branchSpareTransferReceiptService.fetchBranchStoreList(indentToBranchId, userCode);
		if (spareGrnFromResponseList != null && !spareGrnFromResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnFromResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/searchBinName")
	public ResponseEntity<?> searchBinName(@RequestParam() String searchText,
			@RequestParam() Integer indentToBranchId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> spareGrnFromResponseList = 
				branchSpareTransferReceiptService.searchBinName(searchText, indentToBranchId, userCode);
		if (spareGrnFromResponseList != null && !spareGrnFromResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnFromResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody BranchSpareTransferReceiptRequest 
			BranchSpareTransferReceiptRequest,
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
				branchSpareTransferReceiptService.createBranchSpareTransferReceipt(BranchSpareTransferReceiptRequest, userCode);
		if (branchSpareTransferResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Branch Spare Transfer Receipt on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Spare Transfer Receipt Successfully Created");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Branch Spare Transfer Receipt Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(branchSpareTransferResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
