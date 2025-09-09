package com.hitech.dms.web.model.mobile.attendance.request;

import lombok.Data;

@Data
public class LeaveClockRequest {

	private Integer id;  
	private String leaveStartDate;
	private String leaveEndDate;
	private Integer leavetype;
	private String employeeRemark;
	private String approvalStatus;
	private String approvedBy;
	private String  approverRemark;
	private String createdDate;
	private Integer createdBy;
	private String modifiedDate;
	private Integer modifiedBy;
}
