package com.hitech.dms.web.entity.branchTransfer.receipt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "PA_INDENT_RECEIPT_HDR")
@Entity(name = "ReceiptHdrEntity")
@Getter
@Setter
public class ReceiptHdrEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private BigInteger id;

	@Column(name = "pa_issue_hdr_id")
	private BigInteger paIssueHdrId;

	@Column(name = "ReceiptNumber")
	private String receiptNumber;

	@Column(name = "ReceiptDate")
	private Date receiptDate;
	
	@Column(name = "ReceiptBy")
	private BigInteger receiptBy;
	
	@Column(name = "ReceiptByBranch")
	private BigInteger receiptByBranch;
	
	@Column(name = "ReceiptRemarks")
	private String receiptRemarks;
	
	@Column(name = "OtherCharges")
	private BigDecimal otherCharges;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private BigInteger createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
}
