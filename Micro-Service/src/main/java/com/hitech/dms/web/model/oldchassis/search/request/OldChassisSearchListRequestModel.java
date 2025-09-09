package com.hitech.dms.web.model.oldchassis.search.request;

import java.util.Date;

import lombok.Data;

@Data
public class OldChassisSearchListRequestModel {

	private Integer pcId;
	private Integer orgHierID;
	private Integer dealerId;
	private Integer branchId;
	private String model;
	private String fromDate;
	private String toDate;
	private String chassisNo;
	private String registrationNo;
	private String custMobileNo;
	private String includeInactive;
	private Integer page;
	private Integer size;	
}
