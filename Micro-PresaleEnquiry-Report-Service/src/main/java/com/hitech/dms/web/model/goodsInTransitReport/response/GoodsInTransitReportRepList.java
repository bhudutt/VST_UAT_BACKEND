package com.hitech.dms.web.model.goodsInTransitReport.response;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import lombok.Data;

@Data
public class GoodsInTransitReportRepList {
	
	 private Status status;
	 private String message;
	 private List<GoodsInTransitReportResponse> result;
	 private Integer count; 
	 private Integer totalResult;

}
