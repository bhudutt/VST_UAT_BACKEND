/**
 * 
 */
package com.hitech.dms.web.controller.activityClaim.inv.create;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.activityClaim.inv.create.dao.ActivityClaimInvCreateDao;
import com.hitech.dms.web.model.activityClaim.invoice.create.request.ActivityClaimInvCreateRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.create.response.ActivityClaimInvCreateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/activityClaim")
@SecurityRequirement(name = "hitechApis")
public class ActivityClaimInvCreateController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvCreateController.class);

	@Autowired
	private ActivityClaimInvCreateDao activityClaimInvCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/createActivityClaimInvoice")
	public ResponseEntity<?> createActivityClaimInvoice(
			@Valid @RequestBody ActivityClaimInvCreateRequestModel requestedData, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityClaimInvCreateResponseModel responseModel = null;
		try {
			responseModel = activityClaimInvCreateDao.createActivityClaimInvoice(userCode, requestedData, device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Activity Claim Invoice Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Activity Claim Invoice Not Create or server side error.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
