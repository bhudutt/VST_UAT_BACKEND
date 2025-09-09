package com.hitech.dms.web.model.invoice;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerDetailsDto {
	private BigInteger customerId;
	private String customerType;
	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String state;
	private String district;
	private String tehsil;
	private String city;
	private String pincode;
	
}
