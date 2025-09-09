package com.hitech.dms.web.model.partrequisition.create.request;

import lombok.Data;

@Data
public class RequisitionPartListRequestModel {

	private Integer branchId;
	private String criteria;
	private String searchOn;
	private String division;
	private String poCategoryCode;
}
