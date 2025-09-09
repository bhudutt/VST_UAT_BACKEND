package com.hitech.dms.web.model.mobile.attendance.response;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

@Data
public class AttendanceClockDtl {
	
//	private String date;
//	
//	private String clockInDate;
//	
//	private String clockInAttendType;
//	
//	private String clockOutDate;
//	
//	private String clockOutAttendType;
//	
//	private String leaveStatus;
//	
//	private String LeaveReason;
//	
	
	private Integer LeaveId;
	private Date loginDate;
	private Time loginTime;
	private Date logoutDate;
	private Date logoutTime;
	private String workingHour;
//	private Date leaveStartDate;
//	private Date leaveEndDate;
	private String employeeRemark; 
	private String loginType;
	private String status;
	private String todayStatus;
	
	

}
