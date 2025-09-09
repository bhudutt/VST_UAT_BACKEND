package com.hitech.dms.web.model.mobile.attendance.request;

import java.util.Date;

import lombok.Data;

@Data
public class AttendanceClockRequest {
	 
	private Integer id;
	
//	@NotNull(message = "Please enter the clockIn attendence type")
	private Integer clockInAttendenceType;
	
	
	private Integer clockOutAttendenceType;
	
//	@NotNull(message = "Please enter the attendance date")
	private String clockInDateTime;
	
//	@NotNull(message = "Please enter the attendance date")
	private String clockOutDateTime;
	
	private String clockinLatitute;
	
	private String clockInLongitute;
	
	private String clockOutLatitute;
	
	private String clockOutLongitute;
	
	private String clockInLocation;
	
	private String clockOutLocation;
	
	private String photoUrl;
	
	private Date createdDate;
	
	private Integer createdBy;
	
	private Date modifiedDate;
	
	private Integer modifiedBy;
}
