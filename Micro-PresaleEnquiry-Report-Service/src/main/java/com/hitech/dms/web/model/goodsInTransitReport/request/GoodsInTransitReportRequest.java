package com.hitech.dms.web.model.goodsInTransitReport.request;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class GoodsInTransitReportRequest {
			
		 private Integer stateId;
		 
		 private Integer dealerId;
		 
		 private Integer branchId;
		 
		 private Date asOnDate;
		 
		 private Integer profitCenterId;
		 
		 private Integer orgHierID;
		 
		 private Integer modelId;
		 
		 private String itemNumber;
		 
		 private String variant;
		 
		 private String includeInActive;
		 
		 private String zone;
		 
		 private String area;
		 
		 private String territory;
		 
		 private Integer page;
		 
		 private Integer size;
		 
}
