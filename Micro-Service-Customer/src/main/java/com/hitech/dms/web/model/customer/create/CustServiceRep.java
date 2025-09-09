package com.hitech.dms.web.model.customer.create;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustServiceRep {

	private BigInteger customerId;
	private String csNumber;
	private String msg;
	private Integer statusCode;
}
