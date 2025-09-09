package com.hitech.dms.web.model.customer.create;

import java.math.BigInteger;

import lombok.Data;
@Data
public class LookupResponse{
	
	private BigInteger valueId;
	
	private String valueCode;
	
	private Integer displayValue;

}
