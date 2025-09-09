package com.hitech.dms.web.model.service.booking.SearchList;

import java.math.BigInteger;

import lombok.Data;
@Data
public class ServiceBookingChassisByBookingnumberResponseModel {

	
	private BigInteger vinId;
	private String chassisNo;
	private BigInteger customerId;
	private String customerName;
	private String customerMobile;
	
	
}
