/**
 * 
 */
package com.hitech.dms.web.controller.quotation.create;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.quotation.create.VehQuoCreateDao;
import com.hitech.dms.web.model.quotation.create.request.VehQuoHDRRequestModel;
import com.hitech.dms.web.model.quotation.create.response.VehQuoResponseModel;
import com.hitech.dms.web.validators.quotation.create.VehQuoCreateValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping( "/quotation" )
@SecurityRequirement(name = "hitechApis")
public class VehQuoCreateController {
	private static final Logger logger = LoggerFactory.getLogger(VehQuoCreateController.class);

	@Autowired
	private VehQuoCreateDao vehQuoCreateDao;
	@Autowired
	private VehQuoCreateValidator vehQuoCreateValidator;

	@InitBinder("vehQuoHDRRequestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(vehQuoCreateValidator);
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping(value = "/createQuotation")
	public ResponseEntity<?> saveVehicleQuotation(@Valid @RequestBody VehQuoHDRRequestModel vehQuoHDRRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		VehQuoResponseModel responseModel = vehQuoCreateDao.saveVehicleQuotation(userCode, vehQuoHDRRequestModel,
				device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Quotation Created on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
