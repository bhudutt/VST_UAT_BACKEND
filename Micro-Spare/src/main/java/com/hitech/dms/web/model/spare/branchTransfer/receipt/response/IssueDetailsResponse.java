package com.hitech.dms.web.model.spare.branchTransfer.receipt.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class IssueDetailsResponse {

	private BigInteger id;
	private String issueNumber;
	private BigInteger paIndHdrId;
	private Date issueDate;
	private String issueRemarks;
	private String issueBy;
	private String indentNumber;
	private Date indentDate;
	private String indentOnBranch;
	private String indentBy;
	private String indentRemarks;
	private Integer totalIssuedQty;
	private BigDecimal totalIssuedValue;
	private Integer totalIssuedPart;
}
