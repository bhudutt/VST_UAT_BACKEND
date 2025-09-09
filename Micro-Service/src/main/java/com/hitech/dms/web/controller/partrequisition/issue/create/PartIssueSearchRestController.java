package com.hitech.dms.web.controller.partrequisition.issue.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.part.issue.search.PartIssueSearchDao;
import com.hitech.dms.web.model.partissue.search.request.PartIssueSearchRequestModel;
import com.hitech.dms.web.model.partissue.search.response.PartIssueSearchListResultResponse;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssueCommonResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/partIssue")
@SecurityRequirement(name = "hitechApis")
public class PartIssueSearchRestController {
    
	@Autowired
	private PartIssueSearchDao partIssueSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/PartIssueSearchList")
	public ResponseEntity<?> PartRequitionSearchList(@RequestBody PartIssueSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PartIssueSearchListResultResponse responseModel = partIssueSearchDao.PartIssueSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchResult() != null
				&& !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Issue Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Issue Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Issue Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value = "/PartIssueViewList")
	public ResponseEntity<?> PartRequitionSearchList(@RequestParam("issueId") Integer issueId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		SparePartIssueCommonResponseModel responseModel = partIssueSearchDao.PartIssueViewList(userCode,
				issueId);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Issue Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Issue Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Issue Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
}
