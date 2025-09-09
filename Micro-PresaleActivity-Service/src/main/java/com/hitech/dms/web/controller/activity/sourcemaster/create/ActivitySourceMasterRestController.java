package com.hitech.dms.web.controller.activity.sourcemaster.create;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.sourcemaster.ActivitySourceMasterDao;
import com.hitech.dms.web.entity.activity.sourcemaster.ActivitySourceMasterEntity;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivityGlCodeListModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterListResponseModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class ActivitySourceMasterRestController {

private static final Logger logger = LoggerFactory.getLogger(ActivitySourceMasterRestController.class);
	
	@Autowired
	private  ActivitySourceMasterDao activitySourceMasterDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/activitySourceMaster")
	public ResponseEntity<?> createActivitySourceMaster(@RequestBody ActivitySourceMasterEntity activitySourceMasterEntity,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivitySourceMasterResponseModel responseModel = activitySourceMasterDao.createActivitySourceMaster(userCode, activitySourceMasterEntity,
				 device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Source Master added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source Master Not Added or server side error.");
		} else if(responseModel.getStatusCode() == 417) {
			codeResponse.setCode("EC417");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	@GetMapping("/fetchActivitySourceMasterList")
	public ResponseEntity<?> fetchActivitySourceMasterList(
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivitySourceMasterListResponseModel> responseModelList = activitySourceMasterDao.fetchActivitySourceMasterList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Source Master List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source Master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source Master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchActivityGLCodeList")
	public ResponseEntity<?> fetchActivityGLCodeList(@RequestParam("pcId") int pcId,
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityGlCodeListModel> responseModelList = activitySourceMasterDao.fetchActivityGLCodeList(userCode,null,pcId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity GL Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source GL Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Source GL Code Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/changeActiveStatus")
    public ResponseEntity<?> changeActiveStatus(@RequestParam ("activityId") Integer activityId,@RequestParam ("isActive") Character isActive,
    		OAuth2Authentication authentication)
	   {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ActivitySourceMasterResponseModel responseModel = activitySourceMasterDao.changeActiveStatus(userCode,activityId,isActive);
				
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Change Active Status Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Change Active Status or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
	
	@GetMapping("/fetchActivityNameList")
	public ResponseEntity<?> fetchActivityNameList(@RequestParam("searchFor") String searchFor,@RequestParam("activityName") String activityName,
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivitySourceMasterResponseModel> responseModelList=activitySourceMasterDao.fetchActivityNameList(userCode,searchFor,activityName);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Name List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Name List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Name List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}


}
