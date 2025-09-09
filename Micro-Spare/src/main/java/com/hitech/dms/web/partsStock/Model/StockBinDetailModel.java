package com.hitech.dms.web.partsStock.Model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class StockBinDetailModel {
	
	
	private BigInteger stockBinDtlId;
	private BigInteger branchId;
	private BigInteger partBranchId;
	private BigInteger stockStoreId;
	private BigInteger stockBinId;
	
	
	

}
