package com.hitech.dms.web.entity.spare.payment.voucher;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name="PA_PAYMENT_REC_DTL")
@Data
public class PaymentVoucherDtlEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="PAY_REC_HRD_ID")
	private Integer payRecHrdId;
	
	@Column(name="RECIEPT_MODE_ID")
	private Integer recieptModeId;
	
	@Column(name="CHEQUE_DD_NO")
	private String chequeDdNo;
	
	@Column(name="CHEQUE_DD_DATE")
	private Date chequeDdDate;
	
	@Column(name="CHEQUE_BANK_ID")
	private Integer chequeBankId;
	
	@Column(name="CARD_TYPE_ID")
	private Integer cardTypeId;
	
	@Column(name="CARD_NO")
	private String cardNo;
	
	@Column(name="CARD_NAME")
	private String cardName;
	
	@Column(name="CARD_DATE")
	private Date cardDate;
	
	@Column(name="TRAN_NO")
	private String tranNo;
	
	@Column(name="TRAN_DATE")
	private Date tranDate;
	
	@Column(name="E_TRAN_NO")
	private String ewTranNo;
	
	@Column(name="E_TRAN_DATE")
	private Date ewTranDate;
	
	@Column(name="E_SERV_PROVIDER")
	private String ewServProvider;
	
	@Column(name="RTGS_TRAN_NO")
	private String rtgsTranNo;
	
	@Column(name="RTGS_TRAN_DATE")
	private Date rtgsTranDate;

	@Column(name="RTGS_TRAN_BANK_ID")
	private Integer rtgsTranBankId;
	
	@Column(name="TOTAL_PENDING_AMT")
	private BigDecimal totalPendingAmt;
	
	@Column(name="TOTAL_SETTLEMENT_AMT")
	private BigDecimal totalSettlementgAmt;
	
	
}
