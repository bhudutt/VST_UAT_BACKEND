package com.hitech.dms.web.model.spare.branchTransfer.issue.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;

import lombok.Data;

@Data
public class BranchSpareTransferIssueRequest {

	private BigInteger paIndHdrId;
	private BigInteger branchId;
	private Date issueDate;
	private String remarks;
	private String issueBy;
	private List<IndentNumberDetails> indentNumberDetails;
	
	private Integer paIssueHdrId;
	private String issueNumber;
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
