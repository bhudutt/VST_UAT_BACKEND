package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SearchStockAdjustmentRequestDto {
	
	private String adjustmentNo;
	
	private String status;
	
	private BigInteger adjustmentDoneBy;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
