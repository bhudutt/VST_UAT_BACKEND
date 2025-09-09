package com.hitech.dms.web.partsStock.Model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class StockHeaderWithPartBranchIdModel {
	
	
	private BigInteger stockBinId;
	private BigInteger branchId;
	private BigInteger stockStoreId;
	private String binName;
	private BigInteger partBranchId;

}
