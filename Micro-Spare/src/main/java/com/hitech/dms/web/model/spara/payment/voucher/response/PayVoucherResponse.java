package com.hitech.dms.web.model.spara.payment.voucher.response;

import lombok.Data;
@Data
public class PayVoucherResponse {
	
	private Integer paymentVoucherId;
	private String paymentVoucherNumber;
	private String msg;
	private Integer statusCode;

}
