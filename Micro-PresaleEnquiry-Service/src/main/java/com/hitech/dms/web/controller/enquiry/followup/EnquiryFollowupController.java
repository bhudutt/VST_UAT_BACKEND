package com.hitech.dms.web.controller.enquiry.followup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.followup.EnquiryFollowupDao;
import com.hitech.dms.web.entity.enquiry.EnquiryFollowupEntity;
import com.hitech.dms.web.model.enquiry.followup.request.EnquiryFollowupRequest;
import com.hitech.dms.web.model.enquiry.followup.request.EnquiryFollowupResponse;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupCreateRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping({"/followup"})
@SecurityRequirement(name = "hitechApis")
public class EnquiryFollowupController {

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private Mapper mapper;
	@Autowired
	private EnquiryFollowupDao enquiryFollowupDao;
	@PostMapping({"/getByEnquiryId"})
	public ResponseEntity<?> fetchEnquiryFollowupDetails(@RequestBody EnquiryFollowupRequest enquiryFollowupRequestModel, OAuth2Authentication authentication) {
	     String userCode = null;
	     if (authentication != null) {
	    	userCode = authentication.getUserAuthentication().getName();
	     }
	     HeaderResponse userAuthResponse = new HeaderResponse();
	     MessageCodeResponse codeResponse = new MessageCodeResponse();
	     SimpleDateFormat formatter = getSimpleDateFormat();
	     List<EnquiryFollowupResponse> result = enquiryFollowupDao.getFollowupDetailsByEnquiryId(enquiryFollowupRequestModel.getEnquiryId());
	     if (result != null && !result.isEmpty()) {
	       codeResponse.setCode("EC200");
	       codeResponse.setDescription("Enquiry Followup Details Fetched successfully on " + formatter.format(new Date()));
	       codeResponse.setMessage("Enquiry Followup Details Fetched successfully");
	     } else {
	       codeResponse.setCode("EC200");
		   codeResponse.setDescription("Enquiry Followup Details Fetch failed on " + formatter.format(new Date()));
		   codeResponse.setMessage("Enquiry Followup Details Fetch failed or error at server side");
	     } 
	     userAuthResponse.setResponseCode(codeResponse);
	     userAuthResponse.setResponseData(result);
	     return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping({"/create"})
	public ResponseEntity<?> createFollowup(@RequestBody FollowupCreateRequestModel followupCreateRequestModel, OAuth2Authentication authentication) {
	     String userCode = null;
	     if (authentication != null) {
	    	userCode = authentication.getUserAuthentication().getName();
	     }
	     HeaderResponse userAuthResponse = new HeaderResponse();
	     MessageCodeResponse codeResponse = new MessageCodeResponse();
	     SimpleDateFormat formatter = getSimpleDateFormat();
	     EnquiryFollowupEntity entity = mapper.map(followupCreateRequestModel, EnquiryFollowupEntity.class, "EnquiryFollowupMapId");
	     
	     String result = enquiryFollowupDao.createEnquiryFollowup(userCode, entity);
	     if (result != null && result.equals("Success")) {
	       codeResponse.setCode("EC200");
	       codeResponse.setDescription("Enquiry Followup Created successfully on " + formatter.format(new Date()));
	       codeResponse.setMessage("Enquiry Followup Created successfully");
	     } else {
	       codeResponse.setCode("EC200");
		   codeResponse.setDescription("Enquiry Followup Creation failed on " + formatter.format(new Date()));
		   codeResponse.setMessage("Enquiry Followup Creation failed or error at server side");
	     } 
	     userAuthResponse.setResponseCode(codeResponse);
	     userAuthResponse.setResponseData(result);
	     return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping({"/update"})
	public ResponseEntity<?> updateFollowup(@RequestBody FollowupCreateRequestModel followupCreateRequestModel, OAuth2Authentication authentication) {
	     String userCode = null;
	     if (authentication != null) {
	    	userCode = authentication.getUserAuthentication().getName();
	     }
	     HeaderResponse userAuthResponse = new HeaderResponse();
	     MessageCodeResponse codeResponse = new MessageCodeResponse();
	     SimpleDateFormat formatter = getSimpleDateFormat();
	     
	     String result = enquiryFollowupDao.updateFollowup(userCode, followupCreateRequestModel);
	     if (result != null && result.equals("Success")) {
	       codeResponse.setCode("EC200");
	       codeResponse.setDescription("Enquiry Followup updated successfully on " + formatter.format(new Date()));
	       codeResponse.setMessage("Enquiry Followup updated successfully");
	     } else {
	       codeResponse.setCode("EC200");
		   codeResponse.setDescription("Enquiry Followup updation failed on " + formatter.format(new Date()));
		   codeResponse.setMessage("Enquiry Followup updation failed or error at server side");
	     } 
	     userAuthResponse.setResponseCode(codeResponse);
	     userAuthResponse.setResponseData(result);
	     return ResponseEntity.ok(userAuthResponse);
	}
}
