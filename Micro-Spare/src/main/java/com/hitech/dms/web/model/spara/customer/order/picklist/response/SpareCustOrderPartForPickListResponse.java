package com.hitech.dms.web.model.spara.customer.order.picklist.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;

import lombok.Data;

@Data
public class SpareCustOrderPartForPickListResponse {
	
		private Integer id;
		
		private Integer partId;
		
		private Integer partBranchId;
		
		private String partNo;
		
		private String partDesc;
		
		private String productSubCategory;
		
		private String binlocation;
		
		private Integer orderQty;
		
		private Integer currentStock;
				
		private String store;
		
		private BigDecimal basicUnitPrice;
		
//		private List<BranchSpareIssueBinStockResponse> binData;
}
