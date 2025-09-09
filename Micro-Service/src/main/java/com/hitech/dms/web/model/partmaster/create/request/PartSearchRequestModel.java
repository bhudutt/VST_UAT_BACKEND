package com.hitech.dms.web.model.partmaster.create.request;

import lombok.Data;

@Data
public class PartSearchRequestModel {
	private String criteria;
	private Integer partCategoryId;
	private String searchOn;
	private String partDivision;
	private String partType;
	private Integer branchId;
	private String partNo;
	private String partCategoryCode;
}
