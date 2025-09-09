package com.hitech.dms.web.controller.machine.stock.search;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.ExcelUtils;
import com.hitech.dms.web.dao.machinestock.search.MachineStockSearchDao;
import com.hitech.dms.web.model.machinestock.search.MachineStockExportResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockListRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockListResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockOverallResultResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResultResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/machinestock")
@SecurityRequirement(name = "hitechApis")
public class MachineStockSearchController {
    
	private static final Logger logger = LoggerFactory.getLogger(MachineStockSearchController.class);
	@Autowired
	private MachineStockSearchDao  machineStockSearchDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/machineStockSearchList")
	public ResponseEntity<?> machineStockSearchList(@RequestBody MachineStockSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		MachineStockSearchResultResponseModel responseModel = machineStockSearchDao.machineStockSearchList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine Stock Overall Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/machineTransitStockSearchList")
	public ResponseEntity<?> machineTransitStockSearchList(@RequestBody MachineStockSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		MachineStockOverallResultResponseModel responseModel = machineStockSearchDao.machineStockOverAllSearchList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine Stock Overall Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Overall Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Overall Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/machineStockSearchOverAllList")
	public ResponseEntity<?> machineStockSearchOverAllList(@RequestBody MachineStockSearchRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		MachineStockOverallResultResponseModel responseModel = machineStockSearchDao.machineStockOverAllSearchList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Machine Stock Overall Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Overall Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Stock Overall Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	
	
}
