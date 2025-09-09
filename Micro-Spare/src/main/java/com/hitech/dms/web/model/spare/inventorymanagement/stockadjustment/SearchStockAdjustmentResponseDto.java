package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SearchStockAdjustmentResponseDto {
	
	private BigInteger id;
	
	private String action;
	
	private String adjustmentNo;
	
	private Date adjustmentDate;
	
	private String status;
	
	private String remarks;
	
	private String branchName;
	
	
	private String adjustmentDoneBy;
	
	private Boolean dealerFlag;
	
	@JsonIgnore
    private Long totalCount;

}
