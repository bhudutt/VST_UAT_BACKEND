package com.hitech.dms.web.model.mobile.attendance.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AttendanceClockRes {
	
	private String statusCode;
	private String msg;
	private String description;
	private List<AttendanceClockDtl> attendanceList = new ArrayList<AttendanceClockDtl>();

}
