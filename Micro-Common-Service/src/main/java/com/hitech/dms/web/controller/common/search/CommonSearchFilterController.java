package com.hitech.dms.web.controller.common.search;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.common.search.CommonFilterPcDao;
import com.hitech.dms.web.model.common.response.CommonHoDetailResponse;
import com.hitech.dms.web.model.common.search.CommonFilterDealerModel;
import com.hitech.dms.web.model.common.search.CommonFilterDealerResponse;
import com.hitech.dms.web.model.common.search.CommonFilterFinalResponse;
import com.hitech.dms.web.model.common.search.CommonFilterHoModel;
import com.hitech.dms.web.model.common.search.CommonFilterModel;
import com.hitech.dms.web.model.common.search.CommonFilterPcListRes;
import com.hitech.dms.web.model.common.search.CommonFilterStateModel;
import com.hitech.dms.web.model.common.search.CommonFilterZoneModel;
import com.hitech.dms.web.model.common.search.CommonTerritoryModel;
import com.hitech.dms.web.model.common.search.DistrictResponse;

@Validated
@RestController
@RequestMapping("/common/new")
public class CommonSearchFilterController {
	
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	
	@Autowired
	CommonFilterPcDao dao;
	
	
	
	
	@PostMapping("/CommonFilterPcList")
	public ResponseEntity<?> fetchCommonHoDetail(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller "+requestModel +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CommonFilterPcListRes responseModel = dao.getPcListCommon(requestModel, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch pc Detail successfully " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch pc Detail successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pc List not fecthed");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	
	// get HoListCommonFilterPcList
	@PostMapping("/CommonFilterHoList")
	public ResponseEntity<?> fetchCommonFIlterHoDetail(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller Ho "+requestModel +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CommonFilterHoModel> responseModel = dao.getCommonHoList(requestModel, userCode);
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
	
	
	@PostMapping("/CommonFilterZoneList")
	public ResponseEntity<?> fetchCommonFIlterZoneDetail(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller Zone "+requestModel +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CommonFilterZoneModel> responseModel = dao.getCommonZoneList(requestModel, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Zone List on  " + formatter.format(new Date()));
			codeResponse.setMessage("Zone List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Zone List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	
	
	@PostMapping("/CommonFilterStateList")
	public ResponseEntity<?> fetchCommonFIlterStateDetail(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller State"+requestModel +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CommonFilterStateModel> responseModel = dao.getCommonStateList(requestModel, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State List on  " + formatter.format(new Date()));
			codeResponse.setMessage("Zone List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	
	@PostMapping("/CommonFilterTerritoryList")
	public ResponseEntity<?> fetchCommonFIlterTerritoryDetail(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get detail at controller territory "+requestModel +" "+userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CommonTerritoryModel> responseModel = dao.getCommonTerritoryList(requestModel, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State List on  " + formatter.format(new Date()));
			codeResponse.setMessage("Zone List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/CommonFilterDealerList")
	public ResponseEntity<?> fetchCommonFIlterDealerList(@RequestBody CommonFilterModel requestModel,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CommonFilterDealerResponse responseModel = dao.getCommonDealerList(requestModel, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer List on  " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/CommonFilterAll")
	public ResponseEntity<?> fetchCommonFIlterforDealer(
			@RequestParam("pcId") BigInteger pcId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) 
			{
		
			String userCode = null;
			if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
				
		  }
			
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CommonFilterFinalResponse responseModel = dao.getCommonFilterAllResponse(pcId, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer List on  " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/CommonDistrictByStateId")
	public ResponseEntity<?> fetchCommonDistrictByStateId(
			@RequestParam("stateId") BigInteger stateId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) 
			{
		
			String userCode = null;
			if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();	
		   }
			
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DistrictResponse>  responseModel = dao.getCommonDistrictByStateIdResponse(stateId, userCode);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State List on  " + formatter.format(new Date()));
			codeResponse.setMessage("State List fetched Successfully ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	

}
