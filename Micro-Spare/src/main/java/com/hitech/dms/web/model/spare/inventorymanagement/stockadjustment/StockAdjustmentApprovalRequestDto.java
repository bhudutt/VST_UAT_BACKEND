package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class StockAdjustmentApprovalRequestDto {
	
	private BigInteger adjustmentId;
	private String approvalStatus;
	private String remarks;
	private String rejectReason;
	private List<ApprovalAdjPartDetail> adPartDetails;
	
	

}
