package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class CustomerOrderPartNoListRequest {
	
	private BigInteger customerId;
	
	private List<BigInteger> partIdList;

}
