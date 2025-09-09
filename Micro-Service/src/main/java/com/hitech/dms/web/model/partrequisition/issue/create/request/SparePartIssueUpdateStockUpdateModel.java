package com.hitech.dms.web.model.partrequisition.issue.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class SparePartIssueUpdateStockUpdateModel {
	
	private BigInteger partBranchId;
	private BigDecimal availableStock;
	private BigDecimal issueQty; 
	private BigDecimal pendingQty;
	private BigDecimal beforePendingQty;
	private BigInteger binId;
	
}
