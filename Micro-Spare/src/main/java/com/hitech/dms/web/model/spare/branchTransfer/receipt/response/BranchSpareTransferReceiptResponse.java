package com.hitech.dms.web.model.spare.branchTransfer.receipt.response;

import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;

import lombok.Data;

@Data
public class BranchSpareTransferReceiptResponse {
	
	private String receiptNumber;
	private int statusCode;
	private String msg;
	private BranchSpareTransferReceiptHdrResponse branchSpareTransferReceiptHdrResponse;
	private List<IndentNumberDetails> indentNumberDetails;
}
