/**
 * 
 */
package com.hitech.dms.web.controller.activity.source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.source.ActivitySourceListDao;
import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;
import com.hitech.dms.web.model.activity.source.response.SourceListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/activity")
public class ActivitySourceListController {
	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceListController.class);

	@Autowired
	private ActivitySourceListDao activitySourceListDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchActivitySourceListByPcId/{pcId}/{isFor}/{isIncludeInActive}")
	public ResponseEntity<?> fetchActivitySourceListByPcId(@PathVariable Integer pcId, String isFor,
			String isIncludeInActive, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivitySourceListResponseModel> responseModelList = activitySourceListDao
				.fetchActivitySourceListByPcId(userCode, pcId, isFor, isIncludeInActive);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Source List By Pc Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source List By Pc Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source List By Pc Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchActivitySourceListByPcId")
	public ResponseEntity<?> fetchActivitySourceListByPcId(@RequestBody ActivitySourceListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			requestModel.setUserCode(userCode);
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivitySourceListResponseModel> responseModelList = activitySourceListDao
				.fetchActivitySourceListByPcId(requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Source List By Pc Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source List By Pc Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source List By Pc Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchSourceList/{pcId}/{isFor}/{isIncludeInActive}")
	public ResponseEntity<?> fetchSourceList(@PathVariable(required = false) Integer pcId, String isFor, String isIncludeInActive,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SourceListResponseModel> responseModelList = activitySourceListDao.fetchSourceList(userCode, pcId, isFor,
				isIncludeInActive);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Source Liston " + formatter.format(new Date()));
			codeResponse.setMessage("Source List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchSourceList")
	public ResponseEntity<?> fetchSourceList(@RequestBody ActivitySourceListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			requestModel.setUserCode(userCode);
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SourceListResponseModel> responseModelList = activitySourceListDao.fetchSourceList(requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Source List on " + formatter.format(new Date()));
			codeResponse.setMessage("Source List  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
