package com.hitech.dms.web.model.spare.branchTransfer.indent.response;

import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;

import lombok.Data;

@Data
public class BranchSpareTransferResponse {
	
	private String indentNumber;
	private int statusCode;
	private String msg;
	private BranchSpareTransferIndentHdrResponse branchSpareTransferIndentHdrResponse;
	private List<IndentNumberDetails> indentNumberDetails;
}
