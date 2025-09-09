package com.hitech.dms.web.controller.enquiry.validate;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hitech.dms.web.dao.enquiry.validate.EnquiryValidateDao;
import com.hitech.dms.web.model.enquiry.validate.request.EnquiryValidateRequestModel;
import com.hitech.dms.web.model.enquiry.validate.response.EnquiryValidateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping({"/enquiry"})
@SecurityRequirement(name = "hitechApis")
public class EnquiryValidateRestController {
	
	@Autowired
	private EnquiryValidateDao enquiryValidateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping({"/validate"})
	public ResponseEntity<?> fetchEnquiryList(@RequestBody EnquiryValidateRequestModel enquiryValidateRequestModel, OAuth2Authentication authentication) {
	     String userCode = null;
	     if (authentication != null) {
	    	userCode = authentication.getUserAuthentication().getName();
	     }
	     HeaderResponse userAuthResponse = new HeaderResponse();
	     MessageCodeResponse codeResponse = new MessageCodeResponse();
	     SimpleDateFormat formatter = getSimpleDateFormat();
	     enquiryValidateRequestModel.setValidationDate(new Date());  
	     EnquiryValidateResponseModel result = enquiryValidateDao.validateEnquiry(userCode, enquiryValidateRequestModel.getEnquiryId(), enquiryValidateRequestModel.getValidationStatus(), enquiryValidateRequestModel.getValidationDate(), enquiryValidateRequestModel.getRemarks());
	     if (result != null) {
	    	 if(result.getSuccessFlag()!=null && result.getSuccessFlag().equals("Y")){
		       codeResponse.setCode("EC200");
		       codeResponse.setDescription("Enquiry Validated on " + formatter.format(new Date()));
	    	 }else{
	    		codeResponse.setCode("EC200");
			    codeResponse.setDescription("Enquiry Validation failed on" + formatter.format(new Date()));
	    	 }
	       codeResponse.setMessage(result.getMsg());
	     } else {
	       codeResponse.setCode("EC200");
		   codeResponse.setDescription("Enquiry Validation failed on" + formatter.format(new Date()));
		   codeResponse.setMessage("Enquiry Validation failed or error at server side");
	     } 
	     userAuthResponse.setResponseCode(codeResponse);
	     userAuthResponse.setResponseData(result);
	     return ResponseEntity.ok(userAuthResponse);
	 }
}
