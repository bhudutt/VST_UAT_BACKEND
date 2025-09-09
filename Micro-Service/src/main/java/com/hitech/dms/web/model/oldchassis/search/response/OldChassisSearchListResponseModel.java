package com.hitech.dms.web.model.oldchassis.search.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OldChassisSearchListResponseModel {
    
	private String action;
	private BigInteger id;
	private String profitCenter;
	private String zone;
	private String state;
	private String territory;
	private String dealership;
	private String branch;
	private String model;
	private String variant;
	private String itemNo;
	private String itemDescription;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String 	registrationNo;
	private String customerName;
	private String customerMobile;
	private String status;
	
}
