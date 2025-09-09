package com.hitech.dms.web.model.stockSummary;

import lombok.Data;

@Data
public class StockSummaryRequest {
	
	
	private Integer stateId;
	private Integer dealerId;
	private Integer branchId;
	private String asOnDate;
	private Integer pcId;
	private Integer orgHierId;
	private Integer modelId;
	private String itemNo;
	private  String includeActive;
	private Integer page;
	private Integer size;

}
