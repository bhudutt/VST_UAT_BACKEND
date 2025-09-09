package com.hitech.dms.web.model.service.report.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class ServiceSearchReportBean {

	private String engineNo;
	private String chassisNo;	
	private String itemNo;
	private String itemDescription;
	private String modelName;
	private Integer parentDealerCode;
	private String parentDealerName;			
	private String dealerState;
	private String customerName;
	private String address1;
	private String mobileNo;
	private String pinCode;
	private String cityDesc;
	private String tehsilDesc;
	private String districtDesc;
	private String stateDesc;
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
	
	
}
