package com.hitech.dms.web.Controller.spare.branchTransfer.issue;

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
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.service.branchSpareTransfer.indent.BranchSpareTransferIndentServiceImpl;
import com.hitech.dms.web.service.branchSpareTransfer.issue.BranchSpareTransferIssueService;
import com.hitech.dms.web.service.spareClaim.SpareClaimService;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;

@RestController
@RequestMapping("/api/v1/branchSpareTransferIssue")
public class CreateBranchSpareTransferIssueController {

	@Autowired
	private BranchSpareTransferIssueService branchSpareTransferIssueService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/searchIndentNumber")
	public ResponseEntity<?> searchIndentNumber(@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> indentNumberList = branchSpareTransferIssueService.searchIndentNumber(searchText,
				userCode);
		if (indentNumberList != null && !indentNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Indent Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(indentNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/hdrAndDtl")
	public ResponseEntity<?> fetchIndentTransferHdrAndDtl(@RequestParam() BigInteger paIndHdrId,
			@RequestParam() String dealerCode, @RequestParam() String page, @RequestParam() String size,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchSpareTransferResponse branchSpareTransferResponse = branchSpareTransferIssueService
				.fetchIssueTransferHdrAndDtl(paIndHdrId, userCode, dealerCode, page, size);
		if (branchSpareTransferResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Indent Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(branchSpareTransferResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody BranchSpareTransferIssueRequest branchSpareTransferIssueRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchSpareTransferResponse branchSpareTransferResponse = branchSpareTransferIssueService
				.createBranchSpareTransferIssue(branchSpareTransferIssueRequest, userCode);
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

	@GetMapping("/fetchAvailableStock")
	public ResponseEntity<?> fetchAvailableStock(@RequestParam(name = "partBranchId") BigInteger partBranchId,
			@RequestParam(name = "partId") BigInteger partId, @RequestParam(name = "branchId") BigInteger branchId,
			@RequestParam(name = "dealerId") BigInteger dealerId,
			@RequestParam(name = "stockBinId") BigInteger stockBinId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<List<BranchSpareIssueBinStockResponse>> apiResponse = branchSpareTransferIssueService
				.fetchAvailableStock(partBranchId, partId, branchId, dealerId, stockBinId, userCode);
		if (apiResponse.getResult() != null) {
			codeResponse.setCode(String.valueOf(apiResponse.getStatus()));
			codeResponse.setDescription("Stock Details on " + formatter.format(new Date()));
			codeResponse.setMessage(apiResponse.getMessage());
		} else {
			codeResponse.setCode(String.valueOf(apiResponse.getStatus()));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(apiResponse.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiResponse.getResult());
		return ResponseEntity.ok(userAuthResponse);
	}
}
