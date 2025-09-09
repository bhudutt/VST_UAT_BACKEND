package com.hitech.dms.web.model.paymentReceipt.create.response;


import java.math.BigDecimal;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptTypeResponseModel {
	private String receiptType;
	private Double amount;
	private Integer receipt_type_id;
	private BigDecimal totalAmount;
}
