package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import lombok.Data;

@Data
public class StockAdjustmentApprovalResponseDto {
	
	private String msg;
	private Integer statusCode;
	private String approvalStatus;

}
