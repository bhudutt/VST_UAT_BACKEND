/**
 * 
 */
package com.hitech.dms.web.controller.activityplan.upload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activityplan.upload.ActivityPlanUploadDao;
import com.hitech.dms.web.model.activityplan.approval.response.ActivityPlanDateResponse;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;
import com.hitech.dms.web.model.activityplan.upload.response.ActivityPlanUploadResponseModel;
import com.hitech.dms.web.validators.activityplan.upload.ActivityPlanUploadValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activityPlan")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanUploadController.class);

	@Autowired
	private ActivityPlanUploadValidator bindingResultValidator;

	@InitBinder("activityPlanUploadRequestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(bindingResultValidator);
	}

	@Autowired
	private ActivityPlanUploadDao activityPlanUploadDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/uploadActivityPlan")
	public ResponseEntity<?> uploadActivityPlan(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart ActivityPlanUploadRequestModel activityPlanUploadRequestModel,
			@RequestPart(required = false) List<MultipartFile> files, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		boolean isFileExist = true;
		if (files == null || files.size() <= 0 || files.isEmpty()) {

			isFileExist = false;
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanUploadResponseModel responseModel = null;
		if (isFileExist) {
			if (bindingResult.hasErrors()) {
				codeResponse.setCode("EC400");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Activity Plan Not Added or server side error.");

				ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
				errorDetails.setCount(bindingResult.getErrorCount());
				errorDetails.setStatus(HttpStatus.BAD_REQUEST);
				List<String> errors = new ArrayList<>();
				bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
				errorDetails.setErrors(errors);

				userAuthResponse.setResponseCode(codeResponse);
				userAuthResponse.setResponseData(errorDetails);

				return ResponseEntity.ok(userAuthResponse);
			}
			responseModel = activityPlanUploadDao.validateUploadedFile(authorizationHeader, userCode,
					activityPlanUploadRequestModel, files);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Activity Plan .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Activity Plan .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	@GetMapping("/userDesignationLevelDesc")
	public ResponseEntity<?> userDesignationLevelDesc(
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityPlanDateResponse bean = activityPlanUploadDao.userDesignationLevelDesc(userCode);
		if (bean != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("User Designation Level desc details on " + formatter.format(new Date()));
			codeResponse.setMessage("User Designation Level desc Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Designation Level desc Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(bean);
		return ResponseEntity.ok(userAuthResponse);
	}
}
