package com.hitech.dms.web.model.spare.sale.invoice.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PartTaxCalCulationResponse {
	
	private Integer partId;
	
	private BigDecimal totalBasePrice;
	
	private BigDecimal cgst;
	
	private BigDecimal cgstAmount;
	
	private BigDecimal sgst;
	
	private BigDecimal sgstAmount;
	
	private BigDecimal igst;
	
	private BigDecimal igstAmount;
	
	private BigDecimal totalGst;
	
	private String store; 
	
	private String binlocation;
	
	private BigInteger balanceQty;
	
	private BigDecimal mrp;

}
