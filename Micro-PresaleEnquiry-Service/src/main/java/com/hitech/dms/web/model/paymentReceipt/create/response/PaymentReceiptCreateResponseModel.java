package com.hitech.dms.web.model.paymentReceipt.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptCreateResponseModel {
	
	private BigInteger paymentReceiptId;
	private String receiptNo;
	private String msg;
	private int statusCode;

}
