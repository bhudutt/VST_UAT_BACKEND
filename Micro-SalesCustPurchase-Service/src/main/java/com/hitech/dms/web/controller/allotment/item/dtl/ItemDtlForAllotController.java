/**
 * 
 */
package com.hitech.dms.web.controller.allotment.item.dtl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.allotment.item.dtl.ItemDtlForAllotDao;
import com.hitech.dms.web.model.allot.item.dtl.request.ChassisNoDtlRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.request.ItemDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ChassisDtlForAllotResponseModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ItemDtlForAllotResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/allot")
@SecurityRequirement(name = "hitechApis")
public class ItemDtlForAllotController {
	private static final Logger logger = LoggerFactory.getLogger(ItemDtlForAllotController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private ItemDtlForAllotDao itemDtlForAllotDao;

	@PostMapping("/fetchItemDetailForAllot")
	public ResponseEntity<?> fetchItemDetailForAllot(@RequestBody ItemDtlForAllotRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ItemDtlForAllotResponseModel responseModel = itemDtlForAllotDao.fetchItemDetailForAllot(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Item Detail For Allotment on " + formatter.format(new Date()));
			codeResponse.setMessage("Item Detail For Allotment Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Item Detail For Allotment Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchChassisNoDetailForAllot")
	public ResponseEntity<?> fetchChassisNoDetailForAllot(@RequestBody ChassisNoDtlRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ChassisDtlForAllotResponseModel responseModel = itemDtlForAllotDao.fetchChassisNoDetailForAllot(userCode,
				requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Chassis No. Detail For Allotment on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis No. Detail For Allotment Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis No. Detail For Allotment Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
