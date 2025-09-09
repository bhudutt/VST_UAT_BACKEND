package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import lombok.Data;
@Data
public class CustomerOrderPartNoRequest {
	
	
	private Integer dealarId;
	
	private Integer partId;
	
	private BigInteger branchId;
	
	private Integer orderQty;
	
	private Integer productCategoryId;
	
	private String partNumber;
	
	private Integer partyBranchId;

}

