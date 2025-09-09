package com.hitech.dms.web.controller.partrequisition.create;

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
import com.hitech.dms.web.dao.partrequisition.search.PartRequisitionSearchDao;
import com.hitech.dms.web.model.partrequisition.search.request.PartRequisitionSearchRequestModel;
import com.hitech.dms.web.model.partrequisition.search.response.PartRequisitionSearchListResultResponse;
import com.hitech.dms.web.model.partrequisition.search.response.SparePartRequisitionViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/workshopRequisition")
@SecurityRequirement(name = "hitechApis")
public class SparePartRequisitionSearchRestController {
	
	@Autowired
	private PartRequisitionSearchDao partRequisitionSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/PartRequisitionSearchList")
	public ResponseEntity<?> PartRequitionSearchList(@RequestBody PartRequisitionSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		PartRequisitionSearchListResultResponse responseModel = partRequisitionSearchDao.PartRequitionSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchResult() != null
				&& !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Requisition Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Requisition Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Requisition Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/PartRequisitionViewList")
	public ResponseEntity<?> PartRequitionSearchList(@RequestParam("requisitionId") Integer requisitionId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		List<SparePartRequisitionViewResponseModel> responseModel = partRequisitionSearchDao.PartRequisitionViewList(userCode,
				requisitionId);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Requisition Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Requisition View Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Requisition View Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	

}
