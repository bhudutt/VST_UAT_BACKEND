/**
 * 
 */
package com.hitech.dms.web.controller.aop.template.upload;

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
import com.hitech.dms.web.dao.aop.template.upload.AopTargetUploadDao;
import com.hitech.dms.web.model.aop.template.upload.request.AopTargetUploadRequestModel;
import com.hitech.dms.web.model.aop.template.upload.response.AopTargetUploadResponseModel;
import com.hitech.dms.web.validator.aop.upload.AopTargetUploadValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/aop")
@SecurityRequirement(name = "hitechApis")
public class AopTargetUploadController {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetUploadController.class);

	@Autowired
	private AopTargetUploadValidator bindingResultValidator;

	@InitBinder("requestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(bindingResultValidator);
	}

	@Autowired
	private AopTargetUploadDao dao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/uploadAopTarget")
	public ResponseEntity<?> validateUploadedFile(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart AopTargetUploadRequestModel requestModel,
			@RequestPart(required = true) List<MultipartFile> files, BindingResult bindingResult,
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
		AopTargetUploadResponseModel responseModel = null;
		if (isFileExist) {
			if (bindingResult.hasErrors()) {
				codeResponse.setCode("EC400");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Aop Target Not Added or server side error.");

				ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
				errorDetails.setCount(bindingResult.getErrorCount());
				errorDetails.setStatus(HttpStatus.BAD_REQUEST);
				List<String> errors = new ArrayList<>();
				bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
				errorDetails.setErrors(errors);

				userAuthResponse.setResponseCode(codeResponse);
				userAuthResponse.setResponseData(errorDetails);

				userAuthResponse.setResponseCode(codeResponse);
				userAuthResponse.setResponseData(responseModel);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
			}
			responseModel = dao.validateUploadedFile(authorizationHeader, userCode, requestModel,
					files);
			userAuthResponse.setResponseData(responseModel);
			if (responseModel != null && responseModel.getStatusCode() == WebConstants.STATUS_CREATED_201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
				
				userAuthResponse.setResponseCode(codeResponse);
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Aop Target .xlsx file.");

				userAuthResponse.setResponseCode(codeResponse);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Aop Target .xlsx file.");

			userAuthResponse.setResponseCode(codeResponse);			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
		return ResponseEntity.ok(userAuthResponse);
	}
}
