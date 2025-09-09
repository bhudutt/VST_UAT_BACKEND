/**
 * 
 */
package com.hitech.dms.web.controller.activity.enq;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.enq.EnquiryListForActivityDao;
import com.hitech.dms.web.model.activity.enq.request.EnquiryListForActivityRequestModel;
import com.hitech.dms.web.model.activity.enq.response.EnquiryListForActivityResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class EnquiryListForActivityController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryListForActivityController.class);

	@Autowired
	private EnquiryListForActivityDao enquiryListForActivityDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchEnquiryListForActivity")
	public ResponseEntity<?> fetchEnquiryListForActvity(@RequestBody EnquiryListForActivityRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquiryListForActivityResponseModel> responseModelList = enquiryListForActivityDao
				.fetchEnquiryListForActvity(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry List For Activity Plan on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry List For Activity Plan Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry List For Activity Plan Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
