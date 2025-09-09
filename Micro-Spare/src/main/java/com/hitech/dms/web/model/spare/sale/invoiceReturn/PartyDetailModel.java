package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyDetailModel {
	
	private Integer partBranchId;
	private Integer partId;
	private Integer branchId;
	private Integer onHandQty;
	private BigInteger stockBinId;
	private Integer stockStoreId;
	
	
}
