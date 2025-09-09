package com.hitech.dms.web.controller.partreturn.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.hitech.dms.web.dao.partreturn.search.PartReturnSearchDao;
import com.hitech.dms.web.model.partreturn.request.PartReturnSearchRequestModel;
import com.hitech.dms.web.model.partreturn.response.PartReturnSearchListResultResponse;
import com.hitech.dms.web.model.partreturn.response.PartReturnViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/partReturn")
@SecurityRequirement(name = "hitechApis")
public class PartReturnSearchRestController {
    
	@Autowired
	private PartReturnSearchDao partReturnSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/PartReturnSearchList")
	public ResponseEntity<?> PartReturnSearchList(@RequestBody PartReturnSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PartReturnSearchListResultResponse responseModel = partReturnSearchDao.PartReturnSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchResult() != null
				&& !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Return Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Return Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Return Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/PartReturnViewList")
	public ResponseEntity<?> PartReturnViewList(@RequestParam("returnId") Integer returnId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		List<PartReturnViewResponseModel> responseModel = partReturnSearchDao.PartReturnViewList(userCode,
				returnId);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Return View List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Return View Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Return View Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
