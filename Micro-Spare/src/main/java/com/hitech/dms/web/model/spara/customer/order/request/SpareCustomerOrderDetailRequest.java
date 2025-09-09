package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class SpareCustomerOrderDetailRequest {
	
	
	private BigInteger id;
	
	private BigInteger partId;
	
	private BigInteger orderQty;
	
	private BigInteger invoicedQty;
	
	private BigInteger partBranchId;
	
	private BigDecimal basicUnitPrice;
	
	private Integer igst;
	
	private Integer cgst;
	
	private Integer sgst;
	
	
}
