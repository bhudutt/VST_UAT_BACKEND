package com.hitech.dms.web.controller.partrequisition.issue.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.hitech.dms.web.dao.partrequisition.issue.create.PartRequisitionIssueCreateDao;
import com.hitech.dms.web.entity.partrequisition.SparePartIssueEntity;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceMasterResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SpareParSearchtJobCardRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateSingleBinRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateStockRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartRequisitionIssueRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionNumberResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SpareParSearchtJobCardResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartDetailResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartIssureAvailableStockResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueTypeResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionNoSearchResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartSearchtDTLJobCardResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/partIssue")
@SecurityRequirement(name = "hitechApis")
public class SparePartRequisitionIssueCreateRestController {
	private static final Logger logger = LoggerFactory.getLogger(SparePartRequisitionIssueCreateRestController.class);
    @Autowired
	private PartRequisitionIssueCreateDao partRequisitionIssueCreateDao;
    
    private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
    
    @PostMapping("/createPartIssue")
	public ResponseEntity<?> createPartIssue(@RequestBody SparePartRequisitionIssueRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePartRequisitionIssueResponseModel responseModel = partRequisitionIssueCreateDao.createPartIssue(requestModel,userCode,device);
		
		if (responseModel!=null && responseModel.getStatusCode()!=null && responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Requisition Issue added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode()!=null && responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Issue Not Added or server side error.");
		}else if(responseModel.getStatusCode()==null) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Issue Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
       
	}
    
    @GetMapping("/fetchIssueType")
	public ResponseEntity<?> fetchIssueType(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartRequisitionIssueTypeResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchIssueType(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Issue Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    // Job cardSearch List
    @PostMapping("/fetchjobCardlist")
    public ResponseEntity<?>fetchjobCardlist(@RequestBody SpareParSearchtJobCardRequestModel requestModel, Device device,OAuth2Authentication authentication)
    {
    	String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareParSearchtJobCardResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchjobCardlist(userCode,device,requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job Card No List on" + formatter.format(new Date()));
			codeResponse.setMessage("Job Card No List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card No List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
    }
    // Job cardSearch DTL List
    @GetMapping("/fetchjobCardDTLlist")
    public ResponseEntity<?>fetchjobCardDTLlist(@RequestParam (value="roId", required=false) Integer roId, @RequestParam ("flag") String flag,@RequestParam (value="requisitionId",required=false) Integer requisitionId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
    {
    	String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartSearchtDTLJobCardResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchjobCardDTLlist(userCode,device,roId, flag,requisitionId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JOB Card List on" + formatter.format(new Date()));
			codeResponse.setMessage("JOB Card List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JOB Card List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
    }
    
    // requisitionDetails
    @GetMapping("/fetchRequisitionDTLlist")
    public ResponseEntity<?>fetchRequisitionDTLlist(@RequestParam (value="roId", required=false) Integer roId, @RequestParam ("flag") String flag,@RequestParam (value="requisitionId",required=false) Integer requisitionId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
    {
    	String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartRequisitionDetailsResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchRequisitionDTLlist(userCode,device,roId, flag,requisitionId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Requistion List on" + formatter.format(new Date()));
			codeResponse.setMessage("Requistion List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requistion List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
    }
    
    // select Field type Requisition search 
    @GetMapping("/fetchPartRequisitionNo")
	public ResponseEntity<?> fetchPartRequisitionNo(@RequestParam ("roId") Integer roId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartRequisitionNumberResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchPartRequisitionNo(userCode,device,roId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Requisition No List on" + formatter.format(new Date()));
			codeResponse.setMessage("Requisition No List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition No List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    // service Type Requisition DTL 
    @GetMapping("/fetchRequisitionByDTLList")
    public ResponseEntity<?> fetchRequisitionByDTLList(@RequestParam ("requisitionId") Integer requisitionId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartRequisitionNoSearchResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchRequisitionByDTLList(userCode,device,requisitionId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Requisition DTL List on" + formatter.format(new Date()));
			codeResponse.setMessage("Requisition No List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition DTL List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    //part details
    @GetMapping("/fetchPartDetailsList")
    public ResponseEntity<?> fetchPartDetailsList(@RequestParam ("requisitionId") Integer requisitionId,  OAuth2Authentication authentication, Device device, HttpServletRequest request)
    {
    	String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartDetailResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchPartDetailsList(userCode,device, requisitionId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part DTL List on" + formatter.format(new Date()));
			codeResponse.setMessage("Requisition No List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part DTL List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
    }
    
    @GetMapping("/fetchAvailableStockList")
    public ResponseEntity<?> fetchAvailableStockList(@RequestParam ("requisitionId") BigInteger requisitionId,@RequestParam ("partBranchId") String partBranchId,  OAuth2Authentication authentication, Device device, HttpServletRequest request)
    {
    	String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartIssureAvailableStockResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchAvailableStockList(userCode,device, requisitionId,partBranchId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Available Stock List on" + formatter.format(new Date()));
			codeResponse.setMessage("Available Stock List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Available Stock List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
    }
    
    @PostMapping("/fetchUpdateStockQuantity")
    public ResponseEntity<?> fetchUpdateStockQuantity(@RequestBody SparePartIssueUpdateStockRequestModel requestModel,
    		OAuth2Authentication authentication)
	   {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		SparePartRequisitionIssueResponseModel responseModel = partRequisitionIssueCreateDao.fetchUpdateStockQuantity(userCode,requestModel);
				
		if (responseModel.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Stock Quantity Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Stock Quantity Update or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
    
    @PostMapping("/fetchUpdateSingleBinStockQty")
    public ResponseEntity<?> fetchUpdateSingleBinStockQty(@RequestBody SparePartIssueUpdateSingleBinRequestModel requestModel,
    		OAuth2Authentication authentication)
	   {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		SparePartRequisitionIssueResponseModel responseModel = partRequisitionIssueCreateDao.fetchUpdateSingleBinStockQty(userCode,requestModel);
				
		if (responseModel.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Stock Quantity Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Stock Quantity Update or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
    
    
    @GetMapping("/fetchRequestedByEmpList")
	public ResponseEntity<?> fetchRequestedByEmpList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartRequestedByEmployeeListResponseModel> responseModelList = partRequisitionIssueCreateDao.fetchRequestedByEmpList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Employee List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Employee List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Employee List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
