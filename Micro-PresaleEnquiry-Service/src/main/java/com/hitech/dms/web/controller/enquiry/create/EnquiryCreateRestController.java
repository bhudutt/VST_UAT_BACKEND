/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.exceptions.model.ApiError;
import com.hitech.dms.web.dao.enquiry.create.EnquiryCreateDao;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;
import com.hitech.dms.web.validators.enquiry.create.EnquiryCreateValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class EnquiryCreateRestController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryCreateRestController.class);

	@Autowired
	private EnquiryCreateDao enquiryCreateDao;
	@Autowired
	private EnquiryCreateValidator bindingResultValidator;

	@InitBinder("enquiryCreateRequestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(bindingResultValidator);
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/createEnquiry")
	public ResponseEntity<?> createEnquiry(@Valid @RequestBody EnquiryCreateRequestModel enquiryCreateRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//	    MultipartFile multipartFile = multipartRequest.getFile("file");
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryCreateResponseModel responseModel = enquiryCreateDao.createEnquiry(userCode, enquiryCreateRequestModel,
				null, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Enquiry added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(responseModel.getEnquiryHdrId()).toUri();
//vst
//		return ResponseEntity.created(location).build();
	}

	@PostMapping(value = "/createEnquiryWithDocs")
	public ResponseEntity<?> createEnquiryWithFormData(@RequestPart EnquiryCreateRequestModel enquiryCreateRequestModel,
			@RequestPart(required=false) List<MultipartFile> file, BindingResult bindingResult, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		List<EnquiryAttachImagesRequestModel> enquiryAttachImgsList = null;

		if (file != null && file.size() <= 5 && !file.isEmpty()) {
			enquiryAttachImgsList = new ArrayList<EnquiryAttachImagesRequestModel>();
			for (MultipartFile f : file) {
				EnquiryAttachImagesRequestModel attachImagesRequestModel = new EnquiryAttachImagesRequestModel();
				attachImagesRequestModel.setFile(f);
				enquiryAttachImgsList.add(attachImagesRequestModel);
			}
//			enquiryCreateRequestModel.setEnquiryAttachImgsList(enquiryAttachImgsList);
		}
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Not Added or server side error.");

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
		EnquiryCreateResponseModel responseModel = enquiryCreateDao.createEnquiry(userCode, enquiryCreateRequestModel,
				enquiryAttachImgsList, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Enquiry added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
