package com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory;

import com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory.PhysicalInventoryHdr;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentHdr;

import lombok.Data;

@Data
public class StockAdjForPhyInvDto {
	
	PhysicalInventoryHdr physicalInventoryHdr;
	
	SpareStockAdjustmentHdr stockAdjustment;

}
