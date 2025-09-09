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
@Table(name="PA_PAYMENT_REC_GRN_INVOICE_DTL")
@Data
public class PaymentVoucherGrnInvoiceDtlEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="PAY_REC_HRD_ID")
	private Integer payRecHrdId;
	
	@Column(name="GRN_INVOICE_NUMBER")
	private String grnInvId;
	
	@Column(name="GRN_INV_DATE")
	private Date grnInvDate;
	
	@Column(name="GRN_INV_AMT")
	private BigDecimal grnInvAmt;
	
	@Column(name="PENDING_AMT")
	private BigDecimal pendingAmt;
	
	@Column(name="SETTLE_AMT")
	private BigDecimal settleAmt;
	
	@Column(name="SETTLE_DATE")
	private Date settleDate;
	
}
