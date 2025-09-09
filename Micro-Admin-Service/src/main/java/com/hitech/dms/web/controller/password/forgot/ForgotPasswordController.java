/**
 * 
 */
package com.hitech.dms.web.controller.password.forgot;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.forgot.password.ForgotPasswordDao;
import com.hitech.dms.web.model.forgot.password.request.ForgotPasswordRequestModel;
import com.hitech.dms.web.model.forgot.password.response.ForgotPasswordResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "hitechApis")
public class ForgotPasswordController {
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

	@Autowired
	private ForgotPasswordDao forgotPasswordDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestModel requestModel, Device device,
			HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ForgotPasswordResponseModel responseModel = forgotPasswordDao.resetPassword(requestModel);
		if (responseModel != null && responseModel.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Forgot Password Generated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Contact System Administrator.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
