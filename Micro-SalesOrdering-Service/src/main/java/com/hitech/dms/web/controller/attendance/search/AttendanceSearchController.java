package com.hitech.dms.web.controller.attendance.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.controller.grn.search.GrnSearchController;
import com.hitech.dms.web.dao.attendance.search.dao.AttendanceSearchDao;
import com.hitech.dms.web.model.attendance.search.request.AttendanceSearchRequestModel;
import com.hitech.dms.web.model.attendance.search.response.AttendanceSearchResponse;
import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseMainModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/attendance")
@SecurityRequirement(name = "hitechApis")
public class AttendanceSearchController {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceSearchController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	@Autowired
	private AttendanceSearchDao searchDao;
	
	@PostMapping("/attendanceSearch")
	public ResponseEntity<?> attendanceSearch(@RequestBody AttendanceSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		AttendanceSearchResponse responseModel = searchDao.attendanceSearch(userCode, requestModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Attendance Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Attendance Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Attendance Search List Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
