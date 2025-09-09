package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerOrderPartDetailsRequest {
	
	private BigInteger partId;
	private String partNo;
	private BigInteger productId;
	private BigInteger branchId;
	private BigInteger dealarId;
	private BigInteger partyId;
	private BigInteger partyCodeId;
	
}
