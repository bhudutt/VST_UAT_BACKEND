package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigInteger;

import lombok.Data;
@Data
public class PaymentVoucherList {
	
	private BigInteger valueId;
	
	private String valueCode;
	
	private Integer displayValue;
}