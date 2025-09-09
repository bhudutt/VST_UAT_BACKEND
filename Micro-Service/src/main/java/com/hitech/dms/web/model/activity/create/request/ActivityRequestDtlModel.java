package com.hitech.dms.web.model.activity.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ActivityRequestDtlModel {

	private BigInteger jobCardId;
	private String jobCardNo;
	private String jobCardDate;
	private String model;
	private Integer modelId;
	private String chassisNo;
	private String engineNO;
	private String mechanic;
	private String jobCardCategory;
	private String serviceType;
	private BigInteger hoursMeterReding;
	private String customerName;
	private String mobileNo;
	private String tehsil;
	private BigDecimal jobCardBillAmount; 
}
