package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import lombok.Data;

@Data
public class StockAdjustmentListResponse {
	
	private BigInteger partBarnchId;
	
	private BigInteger partId;

	private String partNo;
	
	private String partDesc;
	
	private BigInteger prodCategoryId;
	
	private String productCategory;
	
	private BigInteger prodSubCategoryId;
	
	private String productSubCategory;
	
	private BigInteger stockStoreId;
	
	private BigInteger storeId;
	
	private String store;
	
	private BigInteger binLocationId;
	
	private String binLocation;
	
	private BigDecimal systemStock;
	
	private String adjustmentType;
	
	private BigDecimal adjustmentQty;
	
	private BigDecimal mrp;
	
	private BigDecimal ndp;
	
	private BigDecimal value;
	
	private String reason;
	

	
	
}
