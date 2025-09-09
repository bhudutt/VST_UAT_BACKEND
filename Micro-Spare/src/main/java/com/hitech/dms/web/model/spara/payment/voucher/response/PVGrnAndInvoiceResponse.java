package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PVGrnAndInvoiceResponse {
	
private String grnInvNumber;

	private BigInteger payHeaderId;
	
	private Date grnInvDate;
	
	private BigDecimal grnInvAmt;
	
	private BigDecimal pendingAmt;
	
	private BigDecimal settleAmt;
	
	private String settleDate;
	
	private Integer recordCount;

}
