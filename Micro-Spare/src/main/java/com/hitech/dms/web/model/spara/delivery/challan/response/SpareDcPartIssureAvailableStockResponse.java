package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareDcPartIssureAvailableStockResponse {

	private BigDecimal availableQty;
	private BigInteger stockBinid;
	private Integer storeId;
	private String storeName;
	private BigInteger binId;
	private String binLocation;
	private BigDecimal mrpPrice;
	private BigDecimal cgst;
	private BigDecimal sgst;
	private String partNumber;
	private String partDescription;
	private BigDecimal requestedQty;
	private BigDecimal pendingQty;
	private Integer partBranchId;
	private BigDecimal currentStock;
	private BigInteger requisitionId;
	private Integer totalBranchStock;
	private Integer stockStoreId;
	private Integer partId;
}
