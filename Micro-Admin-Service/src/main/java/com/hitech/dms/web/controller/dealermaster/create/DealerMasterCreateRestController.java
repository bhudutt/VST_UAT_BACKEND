package com.hitech.dms.web.controller.dealermaster.create;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.dealermaster.create.DealerMasterCreateDao;
import com.hitech.dms.web.entity.dealermaster.DealerMasterEntity;
import com.hitech.dms.web.model.dealermaster.create.request.DealerCreateRequestModel;
import com.hitech.dms.web.model.dealermaster.create.request.DealerListRequestModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionCodeListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerGroupListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterCreateResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterSearchListResultResponse;
import com.hitech.dms.web.model.dealermaster.create.response.DealerNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.ProfitCenterUnderUserResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author Sunil.Singh
 *
 */

@RestController
@RequestMapping("/dealermaster")
@SecurityRequirement(name = "hitechApis")
public class DealerMasterCreateRestController {
  
	private static final Logger logger = LoggerFactory.getLogger(DealerMasterCreateRestController.class);
	
	@Autowired
	private DealerMasterCreateDao dealerMasterCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody DealerCreateRequestModel requestedData, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerMasterCreateResponseModel responseModel = null;
		try {
			responseModel = dealerMasterCreateDao.create(userCode, requestedData,device);
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Dealer Master Created on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Dealer Master Not Created or server side error.");
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchDealerList")
	public ResponseEntity<?> fetchDealerList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerListResponseModel> responseModelList = dealerMasterCreateDao.fetchDealerList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchDealerGroupList")
	public ResponseEntity<?> fetchDealerGroupList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerGroupListModel> responseModelList = dealerMasterCreateDao.fetchDealerGroupList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Group List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Group List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Group List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchDealerNameList")
	public ResponseEntity<?> fetchDealerNameList(@RequestParam("dealerCode") String dealerCode,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerNameListModel> responseModelList = dealerMasterCreateDao.fetchDealerNameList(userCode,device,dealerCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Name List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Name List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Name List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchDivisionCodeList")
	public ResponseEntity<?> fetchDivisionCodeList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerDivisionCodeListModel> responseModelList = dealerMasterCreateDao.fetchDivisionCodeList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchDivisionNameList")
	public ResponseEntity<?> fetchDivisionNameList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerDivisionNameListModel> responseModelList = dealerMasterCreateDao.fetchDivisionNameList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Division Name List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Division Name List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Division Name List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	//
	@PostMapping("/getDealerList")
	public ResponseEntity<?> getDealerList(@RequestBody DealerListRequestModel requestModel,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerMasterSearchListResultResponse responseModelList = dealerMasterCreateDao.getDealerList(userCode,device,requestModel);
		if (responseModelList != null && responseModelList.getSearchResult() != null
				&& !responseModelList.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchDealerViewList")
	public ResponseEntity<?> fetchDealerViewList(@RequestParam("batchId") Integer batchId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerListModel> responseModelList = dealerMasterCreateDao.fetchDealerViewList(userCode,device,batchId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer View List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer View List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchProfitCenterList")
	public ResponseEntity<?> fetchProfitCenterList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ProfitCenterUnderUserResponseModel> responseModelList = dealerMasterCreateDao.fetchProfitCenterList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer View List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Dealer View List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/editDealerMaster")
	public ResponseEntity<?> editDealerMaster(@RequestBody @RequestParam(name="status") String status, @RequestParam(name="dealerId") String dealerId, 
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerMasterCreateResponseModel response = dealerMasterCreateDao.editDealerMaster(status, dealerId, userCode);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer List on " + formatter.format(new Date()));
			codeResponse.setMessage("Update Dealer Status Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer status not update or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
}
