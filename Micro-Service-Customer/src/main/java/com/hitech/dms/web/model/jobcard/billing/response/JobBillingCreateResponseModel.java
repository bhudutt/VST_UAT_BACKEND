package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class JobBillingCreateResponseModel {
	
	private String msg;
	private Integer statusCode;
	private BigInteger billingId;
	private String billingNo;
	
}
