package com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PhysicalInventoryDetailDto {
	
	private BigInteger id;
	
	private BigInteger partBranchId;
	
	private BigInteger phyInvId;
	
	private String partNo;
	
	private String partDesc;
	
	private BigInteger productCategoryId;
	
	private String productCategory;
	
	private BigInteger productSubCategoryId;
	
	private String productSubCategory;
	
	private BigInteger fromStoreId;
	
	private String fromStore;
	
	private BigInteger fromBinId;
	
	private String fromStoreBinLocation;
	
	private BigDecimal systemStock;
	
	private BigDecimal physicalInventory;
	
	private BigDecimal diffrenceQty;
	
//	private String adjustmentType;
	
	private BigInteger toStoreId;
	
	private String toStore;
	
	private BigInteger toBinId;
	
	private String toStoreBinLocation;
	
	private BigDecimal netAdustmentQty;
	
//	private BigDecimal mrp;
	
	private BigDecimal ndp;
	
	private BigDecimal diffValue;
	
	private String reason;
	
	private String partLockForTrans;

}
