package com.hitech.dms.web.controller.enquiry.lostDrop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.hitech.dms.web.dao.enquiry.lostDrop.EnquiryLostDropDao;
import com.hitech.dms.web.model.enquiry.lostDrop.request.LostDropRequestModel;
import com.hitech.dms.web.model.enquiry.lostDrop.request.LostDropResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping({"/lostDrop"})
@SecurityRequirement(name = "hitechApis")
public class EnquiryLostDropController {

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private EnquiryLostDropDao enquiryLostDropDao;
	
	@PostMapping({"/resason"})
	public ResponseEntity<?> fetchEnquiryLostDropReason(@RequestBody LostDropRequestModel request, OAuth2Authentication authentication) {
	     String userCode = null;
	     if (authentication != null) {
	    	userCode = authentication.getUserAuthentication().getName();
	     }
	     HeaderResponse userAuthResponse = new HeaderResponse();
	     MessageCodeResponse codeResponse = new MessageCodeResponse();
	     SimpleDateFormat formatter = getSimpleDateFormat();
	     List<LostDropResponseModel> result = enquiryLostDropDao.getLostDropReason(request.getFlag());
	     if (result != null && !result.isEmpty()) {
	       codeResponse.setCode("EC200");
	       codeResponse.setDescription("Enquiry "+request.getFlag()+" Reason Fetched successfully on " + formatter.format(new Date()));
	       codeResponse.setMessage("Enquiry "+request.getFlag()+" Reason Fetched successfully");
	     } else {
	       codeResponse.setCode("EC200");
		   codeResponse.setDescription("Enquiry "+request.getFlag()+" Reason Fetch failed on " + formatter.format(new Date()));
		   codeResponse.setMessage("Enquiry "+request.getFlag()+" Reason Fetch failed or error at server side");
	     } 
	     userAuthResponse.setResponseCode(codeResponse);
	     userAuthResponse.setResponseData(result);
	     return ResponseEntity.ok(userAuthResponse);
	}
}
