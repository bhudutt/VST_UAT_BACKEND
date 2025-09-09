package com.hitech.dms.web.model.spare.branchTransfer.issue.response;

import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;

import lombok.Data;

@Data
public class BranchSpareTransferIssueResponse {
	
	private String indentNumber;
	private int statusCode;
	private String msg;
	private BranchSpareTransferIssueHdrResponse branchSpareTransferIssueHdrResponse;
	private List<IndentNumberDetails> indentNumberDetails;
}
