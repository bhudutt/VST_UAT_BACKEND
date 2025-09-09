package com.hitech.dms.web.model.servicequotation.create.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceQuotationCloseResponse {
	
	
	private String message;
	private Integer statusCode;
	private BigInteger chasisNo;

}
