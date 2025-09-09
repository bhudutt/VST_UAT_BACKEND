package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class DcUpdateSparePartRequest {
	
	 private Integer partId;		 
	 private Integer orderQty;
	 private Integer issuedQty;
	 private Integer partBranchId;
	 private BigDecimal basicUnitPrice;
	 private String fromStore;
	 private String binList;
     private Integer  customerDtlId;
     private Integer stockBinid;
     private Integer  storeId;
     private Integer  stockStoreId;
     private Integer  branchId;
     private Integer  binId;
     private String picklistnumber;
     private Integer pickListDtlId;
}
