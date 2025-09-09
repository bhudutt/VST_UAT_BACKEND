package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareCustomerOrderCalculationRequest {
	
	
	private Integer dealerId;
	private BigInteger branchId;
	private BigInteger partId;
	private Integer qty;
	private Integer partBranchId;

}
