package com.hitech.dms.web.model.coupon.management;

import lombok.Data;

@Data
public class InvoiceExcelReportRequest {
	
	
	private String fromDate;
	private String toDate;
	private String grnNo;
	private String claimNo;
	private Integer page;
	private Integer size;

}
