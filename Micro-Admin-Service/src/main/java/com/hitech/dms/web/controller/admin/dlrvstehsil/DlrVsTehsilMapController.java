/**
 * 
 */
package com.hitech.dms.web.controller.admin.dlrvstehsil;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.admin.dlrvstehsil.dao.DlrVsTehsilMapDao;
import com.hitech.dms.web.model.admin.dlrvstehsil.request.DlrVsTehsilMapRequestModel;
import com.hitech.dms.web.model.admin.dlrvstehsil.response.DlrVsTehsilMapResponseModel;
import com.hitech.dms.web.validators.admin.dlrvstehsil.DlrVsTehsilMapValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "hitechApis")
public class DlrVsTehsilMapController {
	private static final Logger logger = LoggerFactory.getLogger(DlrVsTehsilMapController.class);

	@Autowired
	private DlrVsTehsilMapValidator bindingResultValidator;

	@Autowired
	private DlrVsTehsilMapDao dlrVsTehsilMapDao;

	@InitBinder("requestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(bindingResultValidator);
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/mapDealerVsTehsil")
	public ResponseEntity<?> updateHoUser(@Valid @RequestBody DlrVsTehsilMapRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DlrVsTehsilMapResponseModel responseModel = dlrVsTehsilMapDao.mapDealerVsTehsil(userCode, requestModel);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Tehsils to Dealer Assigned on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsils to Dealer Not Assigned or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
