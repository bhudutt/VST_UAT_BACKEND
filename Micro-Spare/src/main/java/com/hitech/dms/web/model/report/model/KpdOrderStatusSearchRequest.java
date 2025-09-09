package com.hitech.dms.web.model.report.model;

import java.util.Date;

import lombok.Data;

@Data
public class KpdOrderStatusSearchRequest {
	
	private String customerName;
	
	private String kpd;
	
	private String poNo;
	
	private String partNumber;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer page;
	
	private Integer size;
	
	private Integer branchId;

}
