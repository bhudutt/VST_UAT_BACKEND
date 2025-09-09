package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "PA_INV_RETURN_HDR")
@Data
public class PaInvReturnHdrEntity {
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	private String grnDate;
	private String grnFrom;
	private String grnNumber;
	@Column(name = "INV_RET_DATE")
	private String invoiceDate;
	private String invoiceNo;
	private String spareSaleReturnInvoiceNo;
	private String spareSaleReturnInvoiceDate;
	private int dealerId;
	@Column(name = "RETURN_TYPE_ID")
	private String returnTypeId;
	@Column(name = "CLAIM_ID")
	private int claimId;
	@Column(name = "GRN_ID")
	private String grnId;
	@Column(name = "BASIC_AMOUNT")
	private BigDecimal BasicAmount;
	@Column(name = "GST_AMOUNT")
	private BigDecimal gstAmount;
	@Column(name="TOTAL_AMOUNT")
	private BigDecimal totalAmount;
	@Column(name = "CREATEDBY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private String createdDate;
	@Column(name="MODIFIEDBY")
	private String modifiedBy;
	@Column(name = "MODIFIEDDATE")
	private String modifiedDate;

}
