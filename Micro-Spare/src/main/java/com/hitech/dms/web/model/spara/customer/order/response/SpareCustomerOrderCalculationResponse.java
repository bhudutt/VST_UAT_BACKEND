package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SpareCustomerOrderCalculationResponse {
	
	private BigDecimal basicUnitPrice;
	
	private BigDecimal cgst;
	
	private BigDecimal cgstAmount;
	
	private BigDecimal sgst;
	
	private BigDecimal sgstAmount;
	
	private BigDecimal igst;
	
	private BigDecimal igstAmount;
	
	private BigDecimal TotalBasePrice;
	
}
