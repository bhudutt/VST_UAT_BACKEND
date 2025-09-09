package com.hitech.dms.web.model.service.bookingview.request;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceBookingViewRequestModel {

	private BigInteger bookingId;
	private String serviceBookingStatus;
	private Date appointmentDate;
	private String appointmentTime;
	private BigInteger serviceCategory;
	private BigInteger serviceType;
	private BigInteger serviceRepairType;
	private String remarks;
}
