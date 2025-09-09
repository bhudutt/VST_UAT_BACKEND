package com.hitech.dms.web.model.masterdata.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TaxDetailsDTO {

	private BigDecimal cgst;
	private BigDecimal sgst;
	private BigDecimal igst;
	private BigDecimal cgstPercentage;
	private BigDecimal sgstPercentage;
	private BigDecimal igstPercentage;
	private BigDecimal totalChargeAmount;
	private BigDecimal totalAmount;
}
