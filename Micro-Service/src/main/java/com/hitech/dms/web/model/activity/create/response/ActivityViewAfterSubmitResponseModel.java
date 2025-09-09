package com.hitech.dms.web.model.activity.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ActivityViewAfterSubmitResponseModel {
	private Integer activityHdrId;
	private String activityNo;
	private Date activityCreationDate;
	private String activityPlanNumber;
	private Date activityPlanDate;
	private Date activityFromDate;
	private Date activityToDate;
	private Date actualFromDate;
	private Date actualToDate;
	private String pcName;
	private String activityLocation;
	
	private String activityName;
	private String branchName;
	private String jobCardNo;
	private String jobCardDate;
	private String model;
	private String chassisNo;
	private String engine;
	private String mechanic;
	private String jobCardCategory;
	private String serviceType;
	private BigInteger hours;
	private String custName;
	private String mobileNo;
	private String tehsil;
	private BigDecimal jobCardBillAmount;
	//private Integer totalAmount;
	private BigDecimal totalAmount;
	
}
