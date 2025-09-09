package com.hitech.dms.web.model.service.booking.SearchList;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceBookingSearchCustDTLResponseModel {

	private BigInteger customerId;
	private BigInteger customerCategoryId;
	private String customerCategory;
	private String customerName;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String modelName;
	private String registrationNo;	
	private String customerCode;	
	private String mobile;
	private String address;
	private String city;
	private String previousServiceType;
	private Double previousServiceHour;
	private String status;
	//private String nextDueServiceType;
	
}
