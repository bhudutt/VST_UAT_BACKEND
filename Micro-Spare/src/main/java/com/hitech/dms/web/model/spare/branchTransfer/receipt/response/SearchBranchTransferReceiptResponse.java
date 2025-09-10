package com.hitech.dms.web.model.spare.branchTransfer.receipt.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class SearchBranchTransferReceiptResponse {

	private BigInteger id;
	private String receiptNumber;
	private BigInteger paIndHdrId;
	private Date receiptDate;
	private String receiptRemarks;
	private String receiptBy;
	private String OtherCharges;
	private String issueNumber;
	private Date issueDate;
	private String issueBy;
	private String issueByBranch;
	private String issueRemarks;
	private String indentNumber;
	private Date indentDate;
	private String indentOnBranch;
	private String indentBy;
	private Integer indentOnBranchId;
	private Integer indentById;
	private String indentRemarks;
	private Integer totalReceiptQty;
	private BigDecimal totalReceiptValue;
	private Integer totalReceiptPart;
}
