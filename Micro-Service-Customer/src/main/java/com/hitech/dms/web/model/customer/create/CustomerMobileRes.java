package com.hitech.dms.web.model.customer.create;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerMobileRes {

	private BigInteger customerId;
	
	private String customerName;
	
	private String customerCode;
}
