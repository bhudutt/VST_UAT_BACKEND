package com.hitech.dms.web.model.service.report.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerDtlResponse {
	
    private BigInteger Id;
	
	private String customerName;
	
	private String customerCode;
	
	private BigInteger parentDealerId;
	
	private String flag;

}
