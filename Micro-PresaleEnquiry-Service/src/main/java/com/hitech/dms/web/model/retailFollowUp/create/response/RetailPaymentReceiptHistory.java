package com.hitech.dms.web.model.retailFollowUp.create.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailPaymentReceiptHistory {
	
	private String receiptDate;
	private String receiptNo;
	private String receiptType;
	private String receiptMode;
	private BigDecimal receiptAmount;
	private String receiptRemarks;

}
