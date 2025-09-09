package com.hitech.dms.web.model.advancereport;

import lombok.Data;

@Data
public class AdvanceTrackingReportRequestModel {

	private Integer pcId;
	private Integer orgHierID;
	private Integer dealerId;
	private Integer branchId;
	private Integer stateid;
	private String  model;
	private String  loanCash;
	private String financialInstitute;
	private String  fromDate;
	private String  toDate;
	private Integer page;
	private Integer size;	
}
