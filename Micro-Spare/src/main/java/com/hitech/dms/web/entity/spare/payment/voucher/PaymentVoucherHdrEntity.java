package com.hitech.dms.web.entity.spare.payment.voucher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "PA_PAYMENT_REC_HDR")
@Data
public class PaymentVoucherHdrEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="VOUCHER_TYPE_ID")
	private Integer voucherTypeId;
	
	@Column(name="DOC_NO")
	private String docNo;
	
	@Column(name="DOC_DATE")
	private Date docDate;
	
	@Column(name="PARTY_ID")
	private Integer partyId;
	
	@Column(name="BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name="SALE_INVOICE_ID")
	private BigInteger saleInvoiceId;
	
	@Column(name="COUNTER_SALE_PARTY_NAME")
	private String counterSalePartyName;
	
	@Column(name="PARTY_CATG_ID")
	private Integer partyCatgId;
	
	@Column(name="PARTY_CODE")
	private String partyCode;
	
	@Column(name="REF_DOC_ID")
	private Integer refDocId;
	
	@Column(name="AMOUNT")
	private BigDecimal amount;
	
	@Column(name="RECEIPT_MODE_ID")
	private Integer receiptModeId;
	
	@Column(name="ADV_AMT")
	private BigDecimal advAmt;
	
	@Column(name="REMARK")
	private String remark;
	

	@Column(name="ADJUST_AGIN_ADV")
	@Type(type = "yes_no")
	private Boolean adjustAginAdv;
	

	@Column(name="IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

}
