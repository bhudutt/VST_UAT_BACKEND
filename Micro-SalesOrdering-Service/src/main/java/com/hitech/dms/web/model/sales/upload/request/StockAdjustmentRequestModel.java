package com.hitech.dms.web.model.sales.upload.request;


import lombok.Data;

@Data
public class StockAdjustmentRequestModel {

	private Integer pcID;
	private Integer dealerId;
	private String dealerCode;
	private String branchCode;
	private String isInactiveInclude;
}
