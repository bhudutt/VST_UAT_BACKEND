package com.hitech.dms.web.model.coupon.management;

import java.util.Date;

import lombok.Data;

@Data
public class CouponExcelReportRequest {

	private   String fromDate;
	private String  toDate;
	private String userCode;
	private String docNo;
	private String dealerCode;
	private int page;
	private int size;
}
