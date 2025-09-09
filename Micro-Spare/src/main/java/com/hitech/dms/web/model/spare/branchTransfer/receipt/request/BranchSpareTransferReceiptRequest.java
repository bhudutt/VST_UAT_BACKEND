package com.hitech.dms.web.model.spare.branchTransfer.receipt.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.ReceiptIndentDtlDetails;

import lombok.Data;

@Data
public class BranchSpareTransferReceiptRequest {

	private BigInteger paIndHdrId;
	private BigInteger paIssueHdrId;
	private BigInteger branchId;
	private Date receiptDate;
	private String remarks;
	private BigInteger receiptBy;
	private List<ReceiptIndentDtlDetails> receiptIndentDtlDetails;
	
	private Integer paReceiptHdrId;
	private String receiptNumber;
	private String fromDate; 
	private String toDate;
	private Integer page;
	private Integer size;
	private Integer pcId;
	private Integer hoId;
	private Integer zoneId;
	private Integer stateId;
	private Integer territoryId;
}
