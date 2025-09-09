package com.hitech.dms.web.model.spare.search.resquest;

import lombok.Data;

@Data
public class partSerachRequest {
	private String ho;
	private String zone;
	private String state;
	private String territory;
	private String dealership;
	private String userCode;
	private String partNumber;
	private Integer branchId;
	private String fromDate;
	private String toDate;
	private String poType;
	private String poStatus;
	private String productCategory;
	private String poOn;
	private String partyCode;
	private String productSubCategory;
	private String poNumber;
	private String partyName;
	private Integer poOnCheckBoxFlag;
	private Integer page;
	private Integer size;
}
