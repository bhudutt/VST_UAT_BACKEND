package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ServiceBookingChassisListResponseModel {

	private BigInteger vinId;
	private String chassisNumber;
	private BigInteger CustomerId; 
	private BigInteger machineItemId; 
}
