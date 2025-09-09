package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class JobBillingPLOResponseModel {

	private BigDecimal cgst;
	private BigDecimal sgst;
	private BigDecimal igst;
	private BigDecimal cgstPercentage;
	private BigDecimal  sgstPercentage;
	private BigDecimal igstPercentage;
	private BigDecimal totalChargeAmount;
	private BigDecimal totalAmount;
}
