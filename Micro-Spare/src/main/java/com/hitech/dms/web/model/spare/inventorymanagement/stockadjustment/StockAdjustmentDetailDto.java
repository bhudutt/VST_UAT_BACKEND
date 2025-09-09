package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class StockAdjustmentDetailDto {
	
	private BigInteger adjustmentId;
	
	private BigInteger adjustmentDtlId;
	
	private String partNo;
	
	private String partDesc;
	
	private String productCategory;
	
	private String productSubCategory;
	
	private String store;
	
	private String storeBinLocation;
	
	private BigDecimal systemStock;
	
	private String adjustmentType;
	
	private BigDecimal netAdustmentQty;
	
	private BigDecimal mrp;
	
	private BigDecimal ndp;
	
	private BigDecimal value;
	
	private String reason;
	
	private BigInteger saDtlId;
	
	private BigInteger branchId;
	
	private Integer partId;
	
	private Integer storeId;

}
