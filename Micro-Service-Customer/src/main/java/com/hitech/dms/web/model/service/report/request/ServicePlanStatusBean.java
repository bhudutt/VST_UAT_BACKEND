package com.hitech.dms.web.model.service.report.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ServicePlanStatusBean {
	
	private String profitCenter;
	private String engineNo;
	private String chassisNo;	
	private String itemNo;
	private String itemDescription;
	private String modelName;
	private Integer DealerCode;
	private String DealerName;			
	private String dealerState;
	private String dealerDistrict;
	private String customerName;
	private String address1;
	private String mobileNo;
	private String pinCode;
	private String city;
	private String tehsil;
	private String district;
	private String state;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date deliveryChallanDate;
	private String previousServiceName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date previousServiceDate;
	private Integer previousServiceHours;
	private String nextServiceName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date nextServiceDate;
	private Integer noOfDaysFromDateOfDelivery;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@JsonProperty("PDI")
	private Date PDI;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date installation;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_1;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_2;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_3;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_4;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_5;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_6;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_7;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date service_8;
	

	
}
