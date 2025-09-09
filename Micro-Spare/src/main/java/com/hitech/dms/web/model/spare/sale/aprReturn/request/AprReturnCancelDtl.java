package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class AprReturnCancelDtl {
	
	private BigInteger aprDetailId;
	
	private BigInteger aprReturedId;
	
	private BigInteger partBranchId;
	
	private BigInteger branchId;
	
	private BigInteger partId;
	
	private BigInteger stockBinId;
	
	private BigInteger stockStoreId;
	
	private Integer aprReturnQty;
	
	private BigInteger invoiceDetailId;
	
	
}
