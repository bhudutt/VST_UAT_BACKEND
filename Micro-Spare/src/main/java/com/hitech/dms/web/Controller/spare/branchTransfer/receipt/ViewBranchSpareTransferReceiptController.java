package com.hitech.dms.web.Controller.spare.branchTransfer.receipt;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.BranchSpareTransferReceiptResponse;
import com.hitech.dms.web.service.branchSpareTransfer.indent.BranchSpareTransferIndentServiceImpl;
import com.hitech.dms.web.service.branchSpareTransfer.issue.BranchSpareTransferIssueService;
import com.hitech.dms.web.service.branchSpareTransfer.receipt.BranchSpareTransferReceiptService;


@RestController
@RequestMapping("/api/v1/branchSpareTransferReceipt/view")
public class ViewBranchSpareTransferReceiptController {


	@Autowired
	private BranchSpareTransferReceiptService branchSpareTransferReceiptService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/hdrAndDtl")
	public ResponseEntity<?> fetchReceiptTransferHdrAndDtl(@RequestParam() BigInteger paReceiptId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		BranchSpareTransferReceiptResponse branchSpareTransferReceiptResponse = 
				branchSpareTransferReceiptService.fetchReceiptTransferHdrAndDtl(paReceiptId);
		if (branchSpareTransferReceiptResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Issue Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Issue Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Issue Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(branchSpareTransferReceiptResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
