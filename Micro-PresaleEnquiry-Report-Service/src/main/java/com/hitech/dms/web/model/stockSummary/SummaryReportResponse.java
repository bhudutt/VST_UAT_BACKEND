package com.hitech.dms.web.model.stockSummary;

import lombok.Data;

@Data
public class SummaryReportResponse {

	private String zone;
	private String state;
	private String territory;
	private String dealerShip;
	private String parentDealerCode;
	private String parentDealerLocation;
	private String model;
	private String itemNo;
	private String itemDescription;
	private String profitCenter;
	private Integer stockQuantity;
	private Integer deliveryMtd;
	private String deliveryYtd;
	
	
	
	
}
