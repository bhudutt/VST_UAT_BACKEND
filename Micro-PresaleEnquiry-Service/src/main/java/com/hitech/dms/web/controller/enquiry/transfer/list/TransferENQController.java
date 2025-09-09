/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.transfer.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hitech.dms.web.dao.enquiry.transfer.list.EnquiryTransferListDao;
import com.hitech.dms.web.model.enquiry.activity.request.ActivityListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityListResponseModel;
import com.hitech.dms.web.model.enquiry.transfer.request.TransferENQRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.TransferENQResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class TransferENQController {
	private static final Logger logger = LoggerFactory.getLogger(TransferENQController.class);

	@Autowired
	private EnquiryTransferListDao enquiryTransferListDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchTransferEnqList")
	public ResponseEntity<?> fetchEnqSourcesList(@RequestBody TransferENQRequestModel transferENQRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<TransferENQResponse> responseModelList = enquiryTransferListDao.fetchTransferENQList(userCode,
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

}
