package com.hitech.dms.web.model.spare.branchTransfer.indent.request;

import lombok.Data;

@Data
public class BranchSpareTransferIndentRequest {

	private Integer paIndHdrId;
	private String indentNumber;
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
