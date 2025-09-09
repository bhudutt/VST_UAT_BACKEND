package com.hitech.dms.web.model.partmaster.create.request;

import java.math.BigDecimal;


import lombok.Data;

@Data
public class PartDetailsDTO {

	private Integer branchId;
	private String partNo;
	private String partDescription;
	private Integer partId;
	private Integer partBranchId;
	private BigDecimal mrp;
	private BigDecimal ndp;

	private BigDecimal currentStock;
	private BigDecimal totalStock;
	private String uom;
	private String docType;
	private Integer jobCardId;
	private BigDecimal stockAmount;
	private BigDecimal landedCost;
	private String binName;
	private Integer stockBinId;
	private Integer stockStoreId;

	private BigDecimal sellingPrice;
	private String errorMsg;
	private BigDecimal minLevelQty;
	private BigDecimal maxLevelQty;
	private BigDecimal reorderLevelQty;
	private BigDecimal safetyStockQty;
	private Character allowDecimalInQty;
	private Character lockForTranscation;
}
