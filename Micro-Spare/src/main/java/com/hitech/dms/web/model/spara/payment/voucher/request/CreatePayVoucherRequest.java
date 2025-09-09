package com.hitech.dms.web.model.spara.payment.voucher.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import lombok.Data;

@Data
public class CreatePayVoucherRequest {
	
	private Integer id;
	
	private Integer voucherTypeId;
	
	private String pvTypeValueCode;
	
	private String docNo;
	
	private BigDecimal amount;
	
	private Integer receiptModeId;
	
	private BigDecimal advAmt;
	
	private String remark;

	private Boolean adjustAginAdv;
	
	private Boolean isActive;
	
	private Date createdDate;
	
	private BigInteger createdBy;

	private Date modifiedDate;
	
	private BigInteger modifiedBy;
	
	private List<RefDocListRequest> refDocList = new ArrayList<>();
	
	private BigInteger  branchId;
	
	private String counterSalePartyName;
	
	private BigInteger saleInvoiceId;
	
	private Date   docDate;
	
	private Integer partyId;
	
	private Integer partyCatgId;
	
	private String partyCode;
	
	private Integer refDocId;

	private String chequeDdNo;
	
	private Date chequeDdDate;
	
	private Integer chequeBankId;
	
	private Integer cardTypeId;
	
	private String cardNo;
	
	private String cardName;
	
	private Date cardDate;
	
	private String tranNo;
	
	private Date tranDate;
	
	private String ewTranNo;
	
	private Date ewTranDate;
	
	private String ewServProvider;
	
	private String rtgsTranNo;
	
	private Date rtgsTranDate;

	private Integer rtgsTranBankId;
	
	private BigDecimal totalPendingAmt;
	
	private BigDecimal totalSettlementgAmt;

}
