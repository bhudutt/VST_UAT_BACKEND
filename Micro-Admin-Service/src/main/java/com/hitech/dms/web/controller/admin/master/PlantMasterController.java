package com.hitech.dms.web.controller.admin.master;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.hitech.dms.web.dao.master.PlantMasterDao;
import com.hitech.dms.web.model.master.request.PoPlantMasterRequest;
import com.hitech.dms.web.model.master.response.PlantMasterModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/plantMaster")
@SecurityRequirement(name = "hitechApis")
public class PlantMasterController {

	@Autowired
	PlantMasterDao plantMasterDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@PostMapping(value = "/createPlantCode")
	public ResponseEntity<?> createPlantCode(@RequestBody PoPlantMasterRequest poPlantMasterRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PlantMasterModel plantMasterResponse = plantMasterDao.createPlantCode(poPlantMasterRequest, userCode);
		if (plantMasterResponse != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Plant Master Created on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Master Successfully Created");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Master Not Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(plantMasterResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchPlantMasterList")
	public ResponseEntity<?> fetchPlantMasterList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PoPlantMasterRequest> responseModel = plantMasterDao.fetchPlantMasterList();
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
