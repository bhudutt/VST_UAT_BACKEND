package com.hitech.dms.web.controller.enquiry.digitalenquiry;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.digitalenquiry.create.DigitalEnquiryCreateDao;
import com.hitech.dms.web.entity.digitalenquiry.DigitalEnquiryEntity;
import com.hitech.dms.web.model.digitalReport.request.DigitalEnquiReportModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.request.DigitalEnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryCreateResponseModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class DigitalEnquiryCreateRestController {
 private static final Logger logger = LoggerFactory.getLogger(DigitalEnquiryCreateRestController.class);
	
	@Autowired
	private DigitalEnquiryCreateDao digitalEnquiryCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	//@Valid
	@PostMapping("/createDigitalEnquiry")
	public ResponseEntity<?> createDigitalEnquiry(@RequestBody DigitalEnquiryEntity digitalEnquiryCreateRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DigitalEnquiryCreateResponseModel responseModel = digitalEnquiryCreateDao.createDigitalEnquiry(userCode, digitalEnquiryCreateRequestModel,
				 device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Digital Enquiry added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Digital Enquiry Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	
	@GetMapping("/fetchDigitalFormMasterList")
	public ResponseEntity<?> fetchDigitalFormMasterList(
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DigitalEnquiryListResponseModel> responseModelList = digitalEnquiryCreateDao.fetchDigitalFormMasterList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Digitalplatform Master List on " + formatter.format(new Date()));
			codeResponse.setMessage("Digitalplatform Master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Digitalplatform Master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	

}
