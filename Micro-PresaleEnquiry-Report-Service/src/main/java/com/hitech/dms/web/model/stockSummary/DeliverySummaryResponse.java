package com.hitech.dms.web.model.stockSummary;

import java.util.List;

import lombok.Data;

@Data
public class DeliverySummaryResponse {
	List<deliveryReportResponse> reportList;
	private String statusMessage;
	private Integer statusCode;
	private Integer totalRecord;
	private Integer totalDeliveryMtd;
	private Integer totalDeliveryYtd;

}
