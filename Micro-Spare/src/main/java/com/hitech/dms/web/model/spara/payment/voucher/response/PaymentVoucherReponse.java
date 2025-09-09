package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PaymentVoucherReponse {
	
	private BigInteger id;
	
	private String paymentVoucherNo;
	
	private Date paymentVoucherDate;
	
	private String voucherType;
	
	private String partyCode;
	
	private String partyName;
	
	private BigDecimal amount;

}
