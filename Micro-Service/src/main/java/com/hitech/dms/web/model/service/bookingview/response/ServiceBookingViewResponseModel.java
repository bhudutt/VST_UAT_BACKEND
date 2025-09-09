package com.hitech.dms.web.model.service.bookingview.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ServiceBookingViewResponseModel {

	private String branchName;
	private Date serviceBookingDate;
	private String serviceBookingStatus;
	private Date callDate;
	private BigInteger bookingsourceName;
	private String customerMobileNo;
	private String customerName;
	private String chassisNo;
	private String engineNo;
	private String modeldesc;
	private String vinNo;
	private String registrationNo;
	private Date appointmentDate;
	private String appointmentTime;
	private BigInteger serviceCategory;
	private BigInteger servicetype;
	private BigInteger serviceRepairType;
	private String serviceRemark;
	private String customerCode;
	private String address;
	private String city;
	private String previousServiceType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date previousServiceDate;
	private String previousServiceHour;
	private String nextDueServiceType;
}
