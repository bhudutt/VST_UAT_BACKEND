package com.hitech.dms.web.model.jobcard.billing.request;

import lombok.Data;

@Data
public class JobBillingSearchRequestModel {

	private String jobCardNo;
	private String JobCardFormDate;
	private String JobCardToDate;
	private String saleDate;
	private String billingNumber;
	private String billingFormDate;
	private String billingToDate;
	private String billingStatus;
	private String paymentMode;
	private String saleType;
	private String chassisNo;
	private String engineNo;
	//private String registrationNumber;
	private String customerType;
	private String customerName;
	private Integer page;
	private Integer size;
}
