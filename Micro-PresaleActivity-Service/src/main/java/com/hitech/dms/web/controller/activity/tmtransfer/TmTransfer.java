package com.hitech.dms.web.controller.activity.tmtransfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.tm.dao.TmDao;
import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferRequestModel;
import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferResponseModel;
import com.hitech.dms.web.model.tm.create.response.TmListModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQRequestModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQResponse;


@RestController
@RequestMapping("/tm")
public class TmTransfer {
	
	private static final Logger logger = LoggerFactory.getLogger(TmTransfer.class);
	@Autowired
	private TmDao tmDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/tmList")
	public ResponseEntity<?> fetchTmList(
			@RequestParam(required = false) Long dealerOrBranchID,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<TmListModel> responseModelList = tmDao.fetchTMList(userCode,
				dealerOrBranchID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Tm List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("lm List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("lm List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchTmTransferEnqList")
	public ResponseEntity<?> fetchEnqSourcesList(@RequestBody TmTransferENQRequestModel transferENQRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<TmTransferENQResponse> responseModelList = tmDao.fetchTmTransferENQList(userCode,
				transferENQRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Transfer List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Transfer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Transfer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/tm/transfer")
	public ResponseEntity<?> updateSalesmanEnquiry(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@Valid @RequestBody EnquiryTmTransferRequestModel enquiryTransferRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryTmTransferResponseModel responseModel = tmDao.transferEnqTm(authorizationHeader, userCode,
				enquiryTransferRequestModel, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("TM Transfered on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("TM Not Transfered or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(responseModel.getEnquiryHdrId()).toUri();
//
//		return ResponseEntity.created(location).build();
	}
}
