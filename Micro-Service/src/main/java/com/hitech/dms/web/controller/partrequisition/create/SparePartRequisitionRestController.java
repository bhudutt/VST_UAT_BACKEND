package com.hitech.dms.web.controller.partrequisition.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.hitech.dms.web.dao.partrequisition.create.PartRequisitionCreateDao;
import com.hitech.dms.web.model.partrequisition.create.request.PartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionJobCardListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartDetailsRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequitionJobCardNoByRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.SparePartRequisitionRequestModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionJobCardListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionTypeListModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionVehicleDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequitionJobCardNoByResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionTypeResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/workshopRequisition")
@SecurityRequirement(name = "hitechApis")
public class SparePartRequisitionRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(SparePartRequisitionRestController.class);
	
	@Autowired
	private PartRequisitionCreateDao partRequisitionCreateDao;
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createPartRequisition")
	public ResponseEntity<?> createPartRequisition(@RequestBody SparePartRequisitionRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePartRequisitionResponseModel responseModel = partRequisitionCreateDao.createPartRequisition(userCode,requestModel,device
			);
		
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Requisition added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	@GetMapping("/fetchServiceTypeList")
	public ResponseEntity<?> fetchSeriesList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePartRequisitionTypeResponseModel> responseModelList = partRequisitionCreateDao.fetchServiceTypeList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part Requisition Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Part Requisition Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Part Requisition Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
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
		List<SparePartRequestedByEmployeeListResponseModel> responseModelList = partRequisitionCreateDao.fetchRequestedByEmpList(userCode,device);
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
	
	@PostMapping("/fetchJobCardList")
	public ResponseEntity<?> fetchJobCardList(@RequestBody RequisitionJobCardListRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<RequisitionJobCardListResponseModel> responseModelList = partRequisitionCreateDao.fetchJobCardList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job Card List on" + formatter.format(new Date()));
			codeResponse.setMessage("Job Card List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/fetchJobCardByDTLList")
	public ResponseEntity<?> fetchJobCardByDTLList(@RequestBody RequitionJobCardNoByRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		List<RequitionJobCardNoByResponseModel> responseModel = partRequisitionCreateDao.fetchJobCardByDTLList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service booking Search Customer Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchPartList")
	public ResponseEntity<?> fetchPartList(@RequestBody RequisitionPartListRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<RequisitionPartListResponseModel> responseModelList = partRequisitionCreateDao.fetchPartList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part List on" + formatter.format(new Date()));
			codeResponse.setMessage("Job Part List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card Part Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchPartDetailsList")
	public ResponseEntity<?> fetchPartDTLList(@RequestBody RequisitionPartDetailsRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<RequisitionPartDetailsResponseModel> responseModelList = partRequisitionCreateDao.fetchPartDetailsList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part Details List on" + formatter.format(new Date()));
			codeResponse.setMessage("Job Part Details List Successfully fetched");
		}else if (responseModelList.isEmpty()) {
			codeResponse.setCode("EC407");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Price is not available for this part, kindly contact administration");
		}
		else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card Part Details Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchVehicleDetailsList")
	public ResponseEntity<?> fetchVehicleDetailsList(@RequestParam("chassisNo") String chassisNo,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<RequisitionVehicleDetailsResponseModel> responseModelList = partRequisitionCreateDao.fetchVehicleDetailsList(userCode, chassisNo);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Vehicle Details List on" + formatter.format(new Date()));
			codeResponse.setMessage("Vehicle Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Vehicle Details Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchRequisitionTypeList")
	public ResponseEntity<?> fetchRequisitionTypeList(@RequestParam("roId") Integer roId,@RequestParam("Requisitiontype") Integer Requisitiontype,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<RequisitionTypeListModel> responseModelList = partRequisitionCreateDao.fetchRequisitionTypeList(userCode, roId,Requisitiontype);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Requisition Type List on" + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Requisition Type Not Fetched or Data Not Found.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	 @PostMapping("/PartUpdateRequestedQty")
	    public ResponseEntity<?> PartUpdateRequestedQty(@RequestBody PartListRequestModel requestModel,
	    		OAuth2Authentication authentication)
		   {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			
			SparePartRequisitionResponseModel responseModel = partRequisitionCreateDao.PartUpdateRequestedQty(userCode,requestModel);
					
			if (responseModel.getStatusCode() == 200) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Requested QTY Updated on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Requested QTY Update or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(responseModel);
			return ResponseEntity.ok(userAuthResponse);
		
		}
}
