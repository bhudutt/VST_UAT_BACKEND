package com.hitech.dms.web.model.paymentReceipt.view.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Data
public class PaymentReceiptViewRequestModel {
	private BigInteger id;
	private BigInteger branchId;
	private String isFor;

}
