package com.hitech.dms.web.model.paymentReceipt.search.response;


import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptSearchResponseModel {
	private BigInteger id;
	private BigInteger enquiryId;
	private BigInteger branchId;
	//private BigInteger srNo;
	private String enquiryNumber;
	private BigDecimal enquiryAmount;
	private String receiptNo;
	private String receiptDate;

	private BigDecimal receiptAmount;
	private String receiptMode;
	private String receiptType;

	private String customerCode;
	private String customerName;
	private String mobileMo;
	private String customerType;
	
	private String enquiryDate;
	private String enquiryFrom;
//	private String receiptType;
//	private String prospectType;
	private String sourceOfenquiry;
	private String enquiryStage;
	private String profitCenter;
	private String seriesName;
	private String modelName;
	private String variant;
	private String itemNo;
	private String itemDesc;
//	private String validatedBy;
	
	

}
