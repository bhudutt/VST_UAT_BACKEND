/**
 * 
 */
package com.hitech.dms.web.controller.activity.create;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activity.create.ActualActivityCreateDao;
import com.hitech.dms.web.model.activity.create.request.ActualActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActualActivityResponeModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class ActualActivityCreateController {
	private static final Logger logger = LoggerFactory.getLogger(ActualActivityCreateController.class);

	@Autowired
	private ActualActivityCreateDao actualActivityCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/createActualActivity")
	public ResponseEntity<?> createActualActivity(@RequestBody ActualActivityRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActualActivityResponeModel responseModel = actualActivityCreateDao.createActualActivity(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Created Actual Activity on " + formatter.format(new Date()));
			codeResponse.setMessage("Actual Activity Successfully Created");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Actual Activity or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
