package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerOrderDownloadTemplate {
	
	private BigInteger id;
	
	private Long partNo;
	
	private Long orderQty;
	
}
