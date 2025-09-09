package com.hitech.dms.web.model.report.model;

import java.util.Date;

import lombok.Data;

@Data
public class PoStatusSearchRequest {
	
	private Integer branchId;
	
	private String poNumber;
	
	private String partNumber;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
