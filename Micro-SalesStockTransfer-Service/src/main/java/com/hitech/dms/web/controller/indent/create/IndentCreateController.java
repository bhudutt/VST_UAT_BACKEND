/**
 * 
 */
package com.hitech.dms.web.controller.indent.create;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.indent.create.IndentCreateDao;
import com.hitech.dms.web.model.indent.create.request.IndentCreateRequestModel;
import com.hitech.dms.web.model.indent.create.response.IndentCreateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/indent")
@SecurityRequirement(name = "hitechApis")
public class IndentCreateController {
	private static final Logger logger = LoggerFactory.getLogger(IndentCreateController.class);

//	@Autowired
//	private IndentCreateValidator bindingResultValidator;
//	
//	@InitBinder("requestModel")
//	public void initMerchantOnlyBinder(WebDataBinder binder) {
//		binder.addValidators(bindingResultValidator);
//	}

	@Autowired
	private IndentCreateDao createDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/create")
	public ResponseEntity<?> createEnquiryWithFormData(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody @Valid IndentCreateRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
//		if (errors.hasErrors()) {
//			errors.getFieldErrors().forEach(e -> System.out.println(e.getField() + " : " + e.getDefaultMessage()));
////            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
//        }
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		IndentCreateResponseModel responseModel = createDao.createIndent(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Indent created on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Indent Not Created Or Server Side Error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userAuthResponse);
		}
	}
}
