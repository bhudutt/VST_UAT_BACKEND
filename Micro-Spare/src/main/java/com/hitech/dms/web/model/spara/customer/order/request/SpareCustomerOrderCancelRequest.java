package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareCustomerOrderCancelRequest {
	
	private BigInteger customerId;
	
	private String customerOrderStatus;
	
	private String remark;

}
