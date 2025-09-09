package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceBookingSearchByMobileResponseModel {

	private BigInteger customerId;
	private String customerCode;
	private String displayValue;
	private String prospectType;
	private String mobileNo;
	private String underDealerTerritory;
	private String errorFlag;
	private String errorMsg;
}
