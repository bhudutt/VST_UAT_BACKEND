package com.hitech.dms.web.partsStock.Model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartBranchDetailResponse {
	
	private int partId;
	private int branchId;
	private  int partBranchId;
	private  int stockStoreId;
	private int branchStoreId;
	private int onHandQty;
	private BigInteger currentStock;

}
