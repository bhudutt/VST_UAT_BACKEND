package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceBookingResponseModel {
	private BigInteger id;
	private String bookingNo;
	private String msg;
	private int statusCode;
}
