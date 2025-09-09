package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigInteger;

import lombok.Data;
@Data
public class DcSelectCustomerOrderRequest {
	
	private BigInteger customerId;
	
	private boolean dcSelect;

	

}
