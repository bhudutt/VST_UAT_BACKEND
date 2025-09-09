package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;

import lombok.Data;
@Data
public class ApprovalAdjPartDetail {

	private Integer partId;
	private BigInteger branchId;
	private BigInteger adjustmentDtlId;
	private Integer storeId;
}
