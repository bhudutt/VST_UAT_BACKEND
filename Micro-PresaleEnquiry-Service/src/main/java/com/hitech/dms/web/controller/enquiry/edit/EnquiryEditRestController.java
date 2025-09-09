/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.edit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.edit.EnquiryEditDao;
import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditRequestModel;
import com.hitech.dms.web.model.enquiry.edit.response.EnquiryEditResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class EnquiryEditRestController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryEditRestController.class);

	@Autowired
	private EnquiryEditDao enquiryEditDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/updateEnquiry")
	public ResponseEntity<?> createEnquiry(@Valid @RequestBody EnquiryEditRequestModel enquiryEditRequestModel,
			OAuth2Authentication authentication, Device device) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryEditResponseModel responseModel = enquiryEditDao.updateEnquiry(userCode, enquiryEditRequestModel, null,
				device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Enquiry Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Not Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(responseModel.getEnquiryHdrId()).toUri();
//
//		return ResponseEntity.created(location).build();
	}

	@PostMapping(value = "/updateEnquiryWithDocs")
	public ResponseEntity<?> updateEnquiryWithDocs(@Valid @RequestPart EnquiryEditRequestModel enquiryEditRequestModel,
			@RequestPart(required = false) List<MultipartFile> file, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		List<EnquiryEditAttachImagesRequestModel> enquiryAttachImgsList = null;

		if (file != null && file.size() <= 5 && !file.isEmpty()) {
			enquiryAttachImgsList = new ArrayList<EnquiryEditAttachImagesRequestModel>();
			for (MultipartFile f : file) {
				EnquiryEditAttachImagesRequestModel attachImagesRequestModel = new EnquiryEditAttachImagesRequestModel();
				attachImagesRequestModel.setFile(f);
				enquiryAttachImgsList.add(attachImagesRequestModel);
			}
		}
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		EnquiryEditResponseModel responseModel = enquiryEditDao.updateEnquiry(userCode, enquiryEditRequestModel,
				enquiryAttachImgsList, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Enquiry Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Not Updated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
