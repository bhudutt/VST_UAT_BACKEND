package com.hitech.dms.web.model.spara.payment.voucher.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class RefDocListRequest {
	
	private Integer payHrdId;
	
	private String grnInvId;
	
	private Date grnInvDate;
	
	private BigDecimal grnInvAmt;
	
	private BigDecimal pendingAmt;
	
	private BigDecimal settleAmt;
	
	private Date settleDate;
	
	private BigDecimal totalPendingAmt;
	
	private BigDecimal totalSettlementAmt;
	

}
