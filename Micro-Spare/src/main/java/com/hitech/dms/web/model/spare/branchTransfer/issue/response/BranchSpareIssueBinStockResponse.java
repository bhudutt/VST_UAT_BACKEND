package com.hitech.dms.web.model.spare.branchTransfer.issue.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class BranchSpareIssueBinStockResponse {

	private BigDecimal availableQty;
	private Integer branchStoreId;
	private String storeName;
	private BigInteger binId;
	private String binLocation;
	private BigDecimal unitPrice;
	private Integer partBranchId;
	private BigDecimal issueQty;
	private Integer status;
	private Integer stockStoreId;
	private String message;
}
