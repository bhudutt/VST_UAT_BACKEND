package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class StockAdjustmentViewDto {
	
	private BigInteger id;
	
	private BigInteger branchId;
	
	private String branchName;
	
	private String adjustmentNo;
	
	private Date adjustmentDate;
	
	private String status;
	
	private String remarks;
	
	private String adjustmentDoneBy;
	
	private BigDecimal totatIncreseQty;
	
	private BigDecimal totatIncreseValue;
	
	private BigDecimal totatDecreaseQty;
	
	private BigDecimal totatDecreaseValue;
	
	private List<StockAdjustmentDetailDto> partDetails;

}
