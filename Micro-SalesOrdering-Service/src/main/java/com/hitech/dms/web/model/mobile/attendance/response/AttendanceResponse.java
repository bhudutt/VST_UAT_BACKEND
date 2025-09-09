package com.hitech.dms.web.model.mobile.attendance.response;

import lombok.Data;

@Data
public class AttendanceResponse {
	
	private Integer id;
	private String number;
	private String msg;
	private Integer statusCode;

}
