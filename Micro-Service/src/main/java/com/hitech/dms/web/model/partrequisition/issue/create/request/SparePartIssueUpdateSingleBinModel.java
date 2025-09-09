package com.hitech.dms.web.model.partrequisition.issue.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class SparePartIssueUpdateSingleBinModel {

	private Integer quantity;
	private Integer stockBinId; 
	private Integer partBranchId;
	private Integer  stockStoreId;
	private Integer  partId;
	private Integer  branchId;
	private BigDecimal  basicUnitPrice;
	private BigInteger branchstoreId;
	private BigInteger requisitionId;
	private BigInteger issueId;
	private BigInteger requisitionDTLId;
	private BigDecimal requestedQty;
}
