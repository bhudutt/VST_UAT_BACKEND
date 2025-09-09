/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.paymentReceipt.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptDetailsViewResponse {

//	private BigInteger paymentReceiptId;
//	private BigInteger branchId;
//	private BigInteger enquiryId;
	private String receiptNo;
	private String receiptDate;
	private BigDecimal receiptAmount;
	private BigDecimal enquiryAmount;
//	private BigInteger receiptModeId;
//	private BigInteger receiptTypeId;
	private String remarks;
//	private BigInteger partyMasterd;
//	private BigInteger customerMasterId;
//	private BigInteger coDealerId;
	private String customerName;
	private String contactNumber;
	private String customerBalance;
	private String serviceProvider;
	private String transactionDate;
	private String transactionNo;
	private String cardName;
	private String cardNo;
	private String cardType;
	private String chequeDdBank;
	private String chequeDdDate;
	private String chequeDdNo;
}
