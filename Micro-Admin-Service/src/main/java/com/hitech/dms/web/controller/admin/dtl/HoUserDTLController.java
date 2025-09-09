/**
 * 
 */
package com.hitech.dms.web.controller.admin.dtl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.admin.dtl.dao.HoUserDTLDao;
import com.hitech.dms.web.dao.common.model.CommonHoDetailResponse;
import com.hitech.dms.web.model.admin.dtl.request.HoUserDTLRequestModel;
import com.hitech.dms.web.model.admin.dtl.response.HoUserDTLResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "hitechApis")
public class HoUserDTLController {
	private static final Logger logger = LoggerFactory.getLogger(HoUserDTLController.class);

	@Autowired
	private HoUserDTLDao hoUserDTLDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/fetchHoUserDtl")
	public ResponseEntity<?> fetchHoUserDtl(@RequestBody HoUserDTLRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HoUserDTLResponseModel responseModel = hoUserDTLDao.fetchHoUserDtl(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch HO User Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("HO User Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("HO User Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	//
//	@GetMapping("/getCommonHoDetail")
//	public ResponseEntity<?> fetchCommonHoDetail(@RequestParam(required = false) Integer flag,
//			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		CommonHoDetailResponse responseModel = hoUserDTLDao.fetchCommonHoOrgDetail(flag, userCode);
//		if (responseModel != null) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch HO User Detail on " + formatter.format(new Date()));
//			codeResponse.setMessage("HO User Detail Successfully fetched");
//		} else {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("HO User Detail Not Fetched or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModel);
//		return ResponseEntity.ok(userAuthResponse);
//	}
	
	
}
