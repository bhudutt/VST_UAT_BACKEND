package com.hitech.dms.web.model.servicequotation.create.request;

import lombok.Data;

@Data
public class QuotationExcelRequestModel {
	
	private Integer pcId;
	private Integer orgHierId;
	private Integer zoneId;
	private Integer stateId;
	private Integer dealerId;
	private Integer branchId;
	private  String includeActive;
	private String quotationNo;
	private String fromDate;
	private String toDate;
	private String status;
	private Integer page;
	private Integer size;

}
