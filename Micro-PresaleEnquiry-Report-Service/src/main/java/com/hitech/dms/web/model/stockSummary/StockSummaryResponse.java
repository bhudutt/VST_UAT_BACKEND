package com.hitech.dms.web.model.stockSummary;

import java.util.List;

import lombok.Data;

@Data
public class StockSummaryResponse {
	
	private List<StockReportResponse> stockSummaryList;
	private String statusMessage;
	private Integer statusCode;
	private Integer totalRecord;
	private Integer totalStockQnty;

}
