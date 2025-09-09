package com.hitech.dms.web.model.jobcard.save.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class LabourGstCalcDtl {
	
	private BigInteger labourId;
	
	private BigDecimal unitPrice;
	
	private Integer standardHour;
	
	private BigDecimal basePrice;
	
	private BigDecimal igst;
	
	private BigDecimal sgst;
	
	private BigDecimal cgst;
	
	private BigDecimal igstAmt;
	
	private BigDecimal cgstAmt;
	
	private BigDecimal sgstAmt;
	
	private BigDecimal totalGst;
	
	private BigDecimal totalAmt;
	
}
