/**
 * 
 */
package com.hitech.dms.web.controller.incentive.template.billing.upload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.incentive.template.billing.IncentiveTemplateDao;
import com.hitech.dms.web.model.scheme.template.billing.upload.request.IncentiveBillingRequestModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.response.IncentiveBillingMainResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/scheme")
@SecurityRequirement(name = "hitechApis")
public class ValidateIncentiveTemplateController {
	private static final Logger logger = LoggerFactory.getLogger(ValidateIncentiveTemplateController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@Autowired
	private IncentiveTemplateDao dao;
	
	@PostMapping(value = "/uploadIncentiveTemplate")
	public ResponseEntity<?> validateUploadedFile(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@Valid @RequestPart IncentiveBillingRequestModel requestModel,
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
		IncentiveBillingMainResponseModel responseModel = null;
		if (isFileExist) {
			responseModel = dao.validateIncentiveTemplate(authorizationHeader, userCode, requestModel, files);
			userAuthResponse.setResponseData(responseModel);
			if (responseModel != null && responseModel.getStatusCode() == WebConstants.STATUS_CREATED_201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
				
				userAuthResponse.setResponseCode(codeResponse);
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Incentive Template .xlsx file.");

				userAuthResponse.setResponseCode(codeResponse);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Incentive Template .xlsx file.");

			userAuthResponse.setResponseCode(codeResponse);			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
