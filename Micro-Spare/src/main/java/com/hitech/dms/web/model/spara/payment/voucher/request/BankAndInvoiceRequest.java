package com.hitech.dms.web.model.spara.payment.voucher.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BankAndInvoiceRequest {

	private Integer id;
	
	private Integer payRecHrdId;
	
	private Integer grnInvId;
	
	private Date grnInvDate;
	
	private BigDecimal grnInvAmt;
	
	private BigDecimal pendingAmt;
	
	private BigDecimal settleAmt;
	
	private Date settleDate;
	
	private Integer recieptModeId;
	
	private String chequeDdNo;
	
	private Date chequeDdDate;
	
	private Integer chequeBankId;
	
	private Integer cardTypeId;
	
	private String cardNo;
	
	private String cardName;
	
	private Date cardDate;
	
	private String tranNo;
	
	private Date tranDate;
	
	private String eTranNo;
	
	private Date eTranDate;
	
	private String eServProvider;
	
	private String rtgsTranNo;
	
	private Date rtgsTranDate;

	private Integer rtgsTranBankId;
	
}
