package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceBookingSearchByMobileRequestModel {

	private String customerMobileNumber;
	private String userCode;
	private BigInteger dealerID;
}
