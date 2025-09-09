package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class DcPartQtyCalCulationResponse {
	
	private BigDecimal basicUnitPrice;
	
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
