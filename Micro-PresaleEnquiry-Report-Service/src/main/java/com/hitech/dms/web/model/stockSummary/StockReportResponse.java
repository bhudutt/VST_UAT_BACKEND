package com.hitech.dms.web.model.stockSummary;

import lombok.Data;

@Data
public class StockReportResponse {

	
	private String zone;
	private String state;
	private String territory;
	private String dealerShip;
	private String dealerCode;
	//private String parentDealerLocation;
	private String model;
	private String itemNo;
	private String itemDescription;
	private String profitCenter;
	private Integer stockQuantity;
}
