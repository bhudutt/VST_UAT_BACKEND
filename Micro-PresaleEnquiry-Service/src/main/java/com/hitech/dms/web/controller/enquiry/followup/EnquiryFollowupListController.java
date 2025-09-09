package com.hitech.dms.web.controller.enquiry.followup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.followup.EnquiryFollowupListDao;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping({"/followup"})
@SecurityRequirement(name = "hitechApis")
public class EnquiryFollowupListController {

	private static final Logger logger = LoggerFactory.getLogger(EnquiryFollowupListController.class);

	@Autowired
	private EnquiryFollowupListDao enquiryFollowupListDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	

	@GetMapping("/fetchEnqFollowupList")
	public ResponseEntity<?> fetchEnqSourcesList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<FollowupListResponseModel> followupList = enquiryFollowupListDao.FollowupList(userCode, null);
		if (followupList!=null) {
			codeResponse.setDescription("Followup List on" + formatter.format(new Date()));
			codeResponse.setMessage("Followup List Successfully");
		}else {
			codeResponse.setDescription("Follow Up  Data not available" + formatter.format(new Date()));
			codeResponse.setMessage("Followup List fetch unsuccessfully");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(followupList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
//	@PostMapping("/fetchEnqFollowupList")
//	public ResponseEntity<?> fetchFollowupList(@RequestBody FollowupListRequestModel followupListRequestModel,
//			OAuth2Authentication authentication) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		List<FollowupListResponseModel> responseModelList = enquiryFollowupListDao.fetchFollowupList(userCode,
//				followupListRequestModel);
//		if (responseModelList != null && !responseModelList.isEmpty()) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch FollowUP List on " + formatter.format(new Date()));
//			codeResponse.setMessage("Enquiry FollowUP List Successfully fetched");
//		} else {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Enquiry FollowUP List Not Fetched or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModelList);
//		return ResponseEntity.ok(userAuthResponse);
//	}

}
