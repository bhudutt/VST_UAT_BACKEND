package com.hitech.dms.web.model.mobile.attendance.response;

import lombok.Data;

@Data
public class TodayStatusResponse {
	
	private String status;
	
	private String message;
	
	private AttendanceResponse response;

}
