package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustOrderProductCtgResponseModel {
	
	private String msg;
	private Integer statusCode;
	private String productId;
	private String  productCtgNumber;

}
