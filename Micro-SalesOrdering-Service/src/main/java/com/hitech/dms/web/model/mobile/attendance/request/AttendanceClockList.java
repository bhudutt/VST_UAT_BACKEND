package com.hitech.dms.web.model.mobile.attendance.request;

import lombok.Data;

@Data
public class AttendanceClockList {
	
	private String userCode;
	
	private String startDate;
	
	private String endDate;
	
	private Integer flag;
	
	private Integer pageIndex;
	
	private Integer pageEnd; 

}
