package com.hitech.dms.web.controller.service.booking;

import java.math.BigInteger;
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
import com.hitech.dms.web.dao.service.booking.ServiceBookingSearchDao;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingByBookingNoRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingByBookingNoResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingChassisByBookingnumberRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingChassisByBookingnumberResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchCustDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchCustDTLResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListResultResponse;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchVariantByRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchVariantByResponseModel;
import com.hitech.dms.web.model.service.bookingview.request.ServiceBookingViewRequestModel;
import com.hitech.dms.web.model.service.bookingview.response.ServiceBookingViewResponseModel;
import com.hitech.dms.web.model.service.bookingview.response.ServiceBookingViewStatusResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/serviceBooking")
@SecurityRequirement(name = "hitechApis")
public class ServiceBookingSearchListRestController {
     
	@Autowired
	private ServiceBookingSearchDao bookingSearchDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
		
	@PostMapping(value = "/serviceBookingSearchList")
	public ResponseEntity<?> serviceBookingSearchList(@RequestBody ServiceBookingSearchListRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ServiceBookingSearchListResultResponse responseModel = bookingSearchDao.serviceBookingSearchList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchResult() != null
				&& !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service booking Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchModelDTLList")
	public ResponseEntity<?> fetchModelDTLList(@RequestBody ServiceBookingModelDTLRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingModelDTLResponseModel> responseModelList = bookingSearchDao.fetchModelDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchVariantByModelList")
	public ResponseEntity<?> fetchVariantByModelList(@RequestBody ServiceBookingSearchVariantByRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println(ssRequestModel);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingSearchVariantByResponseModel> responseModelList = bookingSearchDao.fetchVariantByModelList(userCode, ssRequestModel);
		System.out.println(responseModelList);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Variant List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Variant List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchBookingNoList")
	public ResponseEntity<?> fetchBookingNoList(@RequestBody ServiceBookingByBookingNoRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingByBookingNoResponseModel> responseModelList = bookingSearchDao.fetchBookingNoList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
      
	//CHASSISNOBYBOOKINGNO
	
	@PostMapping("/fetchChassisNoByBookingNoList")
	public ResponseEntity<?> fetchChassisNoByBookingNoList(@RequestBody ServiceBookingChassisByBookingnumberRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingChassisByBookingnumberResponseModel> responseModelList = bookingSearchDao.fetchChassisNoByBookingNoList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("CHASSIS BY BOOKING Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("CHASSIS BY BOOKING Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/serviceBookingSearchCustDTLList")
	public ResponseEntity<?> serviceBookingSearchCustDTLList(@RequestBody ServiceBookingSearchCustDTLRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		List<ServiceBookingSearchCustDTLResponseModel> responseModel = bookingSearchDao.serviceBookingSearchCustDTLList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service booking Search Customer Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking Search Customer Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking Search Customer Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	 //Chassis search list
	@PostMapping("/fetchChassisList")
	public ResponseEntity<?> fetchChassisList(@RequestBody ServiceBookingChassisAllListRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingChassisAllListResponseModel> responseModelList = bookingSearchDao.fetchChassisList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on" + formatter.format(new Date()));
			codeResponse.setMessage("Chassis List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchCustomerList")
	public ResponseEntity<?> fetchCustomerList(@RequestBody ServiceBookingCustomerListRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingCustomerListResponseModel> responseModelList = bookingSearchDao.fetchCustomerList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer List on" + formatter.format(new Date()));
			codeResponse.setMessage("Customer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	// Service Booking view response
	
	@GetMapping("/fetchServiceBookingListView")
	public ResponseEntity<?> fetchServiceBookingListView(@RequestParam("bookingId")  BigInteger bookingId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingViewResponseModel> responseModelList = bookingSearchDao.fetchServiceBookingListView(userCode, bookingId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer View List on" + formatter.format(new Date()));
			codeResponse.setMessage("Customer View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer View List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	// update booking appointment
	
	@PostMapping("/fetchBookingAppointmentUpdate")
	public ResponseEntity<?> fetchBookingAppointmentUpdate(@RequestBody ServiceBookingViewRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServiceBookingViewStatusResponseModel responseModelList = bookingSearchDao.fetchBookingAppointmentUpdate(userCode,requestModel);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Appointment Update on " + formatter.format(new Date()));
			codeResponse.setMessage("Appointment Update Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Appointment Update Not Validated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}	
	
	
	
}
