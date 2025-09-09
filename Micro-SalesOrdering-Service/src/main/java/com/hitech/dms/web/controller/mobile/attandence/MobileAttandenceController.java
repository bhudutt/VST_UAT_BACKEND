package com.hitech.dms.web.controller.mobile.attandence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockList;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockRequest;
import com.hitech.dms.web.model.mobile.attendance.request.LeaveClockRequest;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceClockRes;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceResponse;
import com.hitech.dms.web.model.mobile.attendance.response.ClockTypeResponse;
import com.hitech.dms.web.model.mobile.attendance.response.TodayStatusResponse;
import com.hitech.dms.web.service.mobile.attandence.MobileAttandenceService;

@RestController
@RequestMapping("/api/v1/mobile")
public class MobileAttandenceController {
	
	
	@Autowired 
	private MobileAttandenceService service;
	
	
	@GetMapping("/clockType") // correct 
	public ResponseEntity<?> getClockTypeList(OAuth2Authentication authentication){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		List<ClockTypeResponse> response = service.getClockTypeList();
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Clock Type Search List on " );
			codeResponse.setMessage("Clock Type Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Clock Type Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/attendanceClockIn") //correct
	public ResponseEntity<?> saveAttendanceClockIn(@RequestBody AttendanceClockRequest bean, OAuth2Authentication authentication,BindingResult bindingResult){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not created or server side error.");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));
			return ResponseEntity.ok(userAuthResponse);
		}
		
		AttendanceResponse response = service.saveAttendanceClock(bean,userCode);
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Attendance clock save");
			codeResponse.setMessage("Attendance clock save Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to save data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/attendanceClockOut") //correct
	public ResponseEntity<?> saveAttendanceClockOut(@RequestBody AttendanceClockRequest bean, OAuth2Authentication authentication,BindingResult bindingResult){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not created or server side error.");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));
			return ResponseEntity.ok(userAuthResponse);
		}
		
		AttendanceResponse response = service.saveAttendanceClockOut(bean,userCode);
		if (response != null && response.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Attendance clock save");
			codeResponse.setMessage("Attendance clock save Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to save data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	@PostMapping("/leaveClock") 
	public ResponseEntity<?> saveLeaveClock(@RequestBody LeaveClockRequest bean, OAuth2Authentication authentication,BindingResult bindingResult){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not created or server side error.");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));
			return ResponseEntity.ok(userAuthResponse);
		}
		
		AttendanceResponse response = service.saveLeaveClock(bean,userCode);
		if (response != null && response.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Leave clock save");
			codeResponse.setMessage("Leave clock save Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to save data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	@PostMapping("/getAttendanceClockList")
	public ResponseEntity<?> getAttendanceClockList(@RequestBody AttendanceClockList bean, OAuth2Authentication authentication,BindingResult bindingResult){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not created or server side error.");
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));
			return ResponseEntity.ok(userAuthResponse);
		}
		
		AttendanceClockRes response = service.getAttendanceClockList(bean,userCode);
		if (response != null && response.getStatusCode().equals("EC200")) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Attendance clock Detail List");
			codeResponse.setMessage("Fetch attendance clock save Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to fetch data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	@GetMapping("/cancelLeave") // correct 
	public ResponseEntity<?> cancelLeave(@RequestBody @RequestParam("leaveId") Integer leaveId, OAuth2Authentication authentication){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		AttendanceResponse response = service.cancelLeave(leaveId,userCode);
		if (response != null && response.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Apply leave cancelled" );
			codeResponse.setMessage("Apply leave cancelled Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to update data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getLeaveAndAttend") 
	public ResponseEntity<?> getLeaveAndAttend(@RequestBody @RequestParam("requestDate") String requestDate, OAuth2Authentication authentication){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		AttendanceResponse response = service.getLeaveAndAttend(requestDate,userCode);
		if (response != null && response.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Get status from either leave or attendance" );
			codeResponse.setMessage("Get status from either leave or attendance");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to update data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	@GetMapping("/todayStatus") // correct 
	public ResponseEntity<?> getTodayStatus(OAuth2Authentication authentication){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		TodayStatusResponse response = service.getTodayStatus(userCode);
		if (response != null && response.getResponse().getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Get today status" );
			codeResponse.setMessage("Fetch today status succefully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to fetch data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/futureDaysLeave") // correct 
	public ResponseEntity<?> futureDaysLeave(OAuth2Authentication authentication){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		List<String> response = service.futureDaysLeave(userCode);
		if (response != null && !response.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Get future date leave" );
			codeResponse.setMessage("Fetch future date leave succefully...");
		}else if (response.isEmpty()) {
			codeResponse.setCode("EC407");
			codeResponse.setDescription("Get future date leave" );
			codeResponse.setMessage("Fetch future date leave succefully...");
		}
		else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Unable to fetch data or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
	
