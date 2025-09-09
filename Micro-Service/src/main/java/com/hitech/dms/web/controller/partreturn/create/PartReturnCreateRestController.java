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
import com.hitech.dms.web.dao.partreturn.create.PartReturnDao;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnCreateRequestModel;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnQtyRequestModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnCreateResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueDetailsResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnJobCardDetailsResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnJobCardSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnReasionTypeResponseModel;
import com.hitech.dms.web.model.partreturn.response.RestrictSpareReturnListModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/partReturn")
@SecurityRequirement(name = "hitechApis")
public class PartReturnCreateRestController {

	@Autowired
	private PartReturnDao partReturnDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createPartReturn")
	public ResponseEntity<?> createPartReturn(@RequestBody PartReturnCreateRequestModel requestModel, OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartReturnCreateResponseModel responseModel=partReturnDao.createPartReturn(userCode, device,requestModel);
		
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Return added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Return Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
    @GetMapping("/fetchReasonType")
	public ResponseEntity<?> fetchReasonType(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartReturnReasionTypeResponseModel> responseModelList = partReturnDao.fetchReasonType(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Reason Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Reason Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    // job card search
    @GetMapping("/fetchJobCardWorkshopType")
	public ResponseEntity<?> fetchJobCardWorkshopType(@RequestParam("searchId") Integer searchId,@RequestParam("branchId") Integer branchId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartReturnJobCardSearchResponseModel> responseModelList = partReturnDao.fetchJobCardWorkshopType(userCode,device,searchId,branchId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job Card List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Job Card List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
   
    // requisition search
    
    @GetMapping("/fetchIssueSearchList")
   	public ResponseEntity<?> fetchIssueSearchList(@RequestParam("searchId") Integer searchId,@RequestParam("branchId") Integer branchId,
   			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		List<PartReturnIssueSearchResponseModel> responseModelList = partReturnDao.fetchIssueSearchList(userCode,device,searchId,branchId);
   		if (responseModelList != null && !responseModelList.isEmpty()) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Issue Number List on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Issue Number List Successfully fetched");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    // part issue  details
    @GetMapping("/fetchPartReturnDetailsList")
   	public ResponseEntity<?> fetchPartReturnDetailsList(@RequestParam(value="issueId",required=false) Integer issueId,@RequestParam(value="roId",required=false) Integer roId,
   			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		List<PartReturnIssueDetailsResponseModel> responseModelList = partReturnDao.fetchPartReturnDetailsList(userCode,device,issueId,roId);
   		if (responseModelList != null && !responseModelList.isEmpty()) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Issue Details List on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Issue Details List Successfully fetched");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
	
    @PostMapping("/fetchPartReturnQtyUpdate")
   	public ResponseEntity<?> fetchPartReturnQtyUpdate(@RequestBody PartReturnQtyRequestModel partReturnRequestModel,
   			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		PartReturnCreateResponseModel responseModelList = partReturnDao.fetchPartReturnQtyUpdate(userCode,device,partReturnRequestModel);
   		if (responseModelList != null ) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch Issued Qty Update on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch Issued Qty Update Successfully");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("List Not Fetched or server side error.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
    
    // NEW API
    @GetMapping("/fetchRestrictSpareReturnList")
   	public ResponseEntity<?> fetchRestrictSpareReturnList(@RequestParam(value="roId",required=false) Integer roId,@RequestParam(value="flag",required=false) String flag,
   			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
   		String userCode = null;
   		if (authentication != null) {
   			userCode = authentication.getUserAuthentication().getName();
   		}
   		HeaderResponse userAuthResponse = new HeaderResponse();
   		MessageCodeResponse codeResponse = new MessageCodeResponse();
   		SimpleDateFormat formatter = getSimpleDateFormat();
   		List<RestrictSpareReturnListModel> responseModelList = partReturnDao.fetchRestrictSpareReturnList(userCode,device,roId,flag);
   		if (responseModelList != null && !responseModelList.isEmpty()) {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Fetch PCR/GOODWILL List on " + formatter.format(new Date()));
   			codeResponse.setMessage("Fetch PCR/GOODWILL List Successfully fetched");
   		} else {
   			codeResponse.setCode("EC200");
   			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
   			codeResponse.setMessage("List Not Fetched or Data Not Found.");
   		}
   		userAuthResponse.setResponseCode(codeResponse);
   		userAuthResponse.setResponseData(responseModelList);
   		return ResponseEntity.ok(userAuthResponse);
   	}
}
