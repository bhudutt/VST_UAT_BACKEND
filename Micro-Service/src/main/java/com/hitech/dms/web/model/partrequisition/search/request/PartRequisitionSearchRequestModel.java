package com.hitech.dms.web.model.partrequisition.search.request;

import lombok.Data;

@Data
public class PartRequisitionSearchRequestModel {

	
	private String fromDate;
	private String toDate;
	private String requisitionNo;
	private String jobCardNo;
	private String requisitionType;
	private Integer page;
	private Integer size;
	private Integer profitCenterId;
	private Integer dealerId;
	private Integer branchId;
	private Integer orgHierarchyId;
	private Integer zone;
	private Integer stateId;
	private Integer territory;
}
