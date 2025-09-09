package com.hitech.dms.web.model.partreturn.create.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartReturnQtyUpdateModel {

	private Integer returnQty;
	private Integer stockBinId;
	private Integer partBranchId;
	private Integer stockStoreId;
	private Integer partId;
	private Integer branchId;
	private BigDecimal basicUnitPrice;
	private Integer branchStoreId;
	private Integer issuedId;
	private Integer requisitionId;
}
