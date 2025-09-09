package com.hitech.dms.web.entity.branchTransfer.issue;

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

@Table(name = "PA_INDENT_ISSUE_HDR")
@Entity(name = "IssueHdrEntity")
@Getter
@Setter
public class IssueHdrEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private BigInteger id;

	@Column(name = "pa_indent_hdr_id")
	private BigInteger paIndentHdrId;

	@Column(name = "IssueNumber")
	private String issueNumber;

	@Column(name = "IssueDate")
	private Date issueDate;

	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "IssueBy")
	private String issueBy;
	
	@Column(name = "TotalIssuedQty")
	private BigDecimal totalIssuedQty;

	@Column(name = "TotalIssuedPart")
	private BigDecimal totalIssuedPart;
	
	@Column(name = "TotalIssuedValue")
	private BigDecimal totalIssuedValue;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private BigInteger createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
}
