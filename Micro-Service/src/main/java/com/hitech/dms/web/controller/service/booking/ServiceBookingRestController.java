package com.hitech.dms.web.controller.service.booking;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.hitech.dms.web.dao.service.booking.ServiceBookingDao;
import com.hitech.dms.web.entity.customer.ServiceBookingEntity;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCategoryListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingRepairOrderListResponse;
import com.hitech.dms.web.model.service.booking.ServiceBookingRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingServiceTypeListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingStatusResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@RequestMapping("/serviceBooking")
@SecurityRequirement(name = "hitechApis")
public class ServiceBookingRestController {
    
	@Autowired
	private ServiceBookingDao serviceBookingDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createServiceBooking")
	public ResponseEntity<?> createServiceBooking(@Valid @RequestBody ServiceBookingEntity RequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServiceBookingResponseModel responseModel = serviceBookingDao.createServiceBooking(userCode, RequestModel
				);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Booking added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Booking Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	@GetMapping("/fetchBookingSourceList")
	public ResponseEntity<?> fetchBookingSourceList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingSourceListResponseModel> responseModelList = serviceBookingDao.fetchBookingSourceList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Booking Source List on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Source List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchBookingChassisList")
	public ResponseEntity<?> fetchBookingChassisList(@RequestParam("customerID") BigInteger customerID, 
			 OAuth2Authentication authentication,Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingChassisListResponseModel> responseModelList = serviceBookingDao.fetchBookingChassisList(userCode,device,customerID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Booking Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Chassis List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchBookingCategoryList")
	public ResponseEntity<?> fetchBookingCategoryList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingCategoryListResponseModel> responseModelList = serviceBookingDao.fetchBookingCategoryList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Booking Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchBookingServiceTypeList")
	public ResponseEntity<?> fetchBookingServiceTypeList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingServiceTypeListResponseModel> responseModelList = serviceBookingDao.fetchBookingServiceTypeList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Booking Service Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Service  List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Service Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchBookingRepairOrderTypeList")
	public ResponseEntity<?> fetchBookingRepairOrderTypeList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingRepairOrderListResponse> responseModelList = serviceBookingDao.fetchBookingRepairOrderTypeList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Booking Repair Order Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Repair Order List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Booking Repair Order List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchServiceBookingStatusList")
	public ResponseEntity<?> fetchServiceBookingStatusList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingStatusResponseModel> responseModelList = serviceBookingDao.fetchServiceBookingStatusList(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Booking Status List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Booking Status List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Booking Status List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	// status list view
	@GetMapping("/fetchBookingStatusViewList")
	public ResponseEntity<?> fetchBookingStatusViewList(
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingStatusResponseModel> responseModelList = serviceBookingDao.fetchBookingStatusViewList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Booking Status List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Booking Status List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Booking Status List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchMobileList")
	public ResponseEntity<?> fetchMobileList(@RequestBody ServiceBookingSearchByMobileRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingSearchByMobileResponseModel> responseModelList = serviceBookingDao.fetchMobileList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Mobile List on" + formatter.format(new Date()));
			codeResponse.setMessage("Mobile List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Mobile List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
}
