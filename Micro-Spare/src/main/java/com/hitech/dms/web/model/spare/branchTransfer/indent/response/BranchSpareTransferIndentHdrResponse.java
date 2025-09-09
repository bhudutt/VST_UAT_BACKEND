package com.hitech.dms.web.model.spare.branchTransfer.indent.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BranchSpareTransferIndentHdrResponse {

	private BigInteger id;
	private String indentNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date indentDate;
	private String indentOnBranch;
	private String indentBy;
	private String IndentRemarks;
}
