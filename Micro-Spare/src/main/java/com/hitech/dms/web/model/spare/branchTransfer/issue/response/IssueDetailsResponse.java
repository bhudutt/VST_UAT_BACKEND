package com.hitech.dms.web.model.spare.branchTransfer.issue.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class IssueDetailsResponse {

	private BigInteger id;
	private String issueNumber;
//	private BigInteger paIndHdrId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date issueDate;
	private String issueRemarks;
	private String issueBy;
	private String indentNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date indentDate;
	private String indentOnBranch;
//	private String indentBy;
	private String indentRemarks;
//	private Integer totalIssuedQty;
//	private BigDecimal totalIssuedValue;
//	private Integer totalIssuedPart;
}
