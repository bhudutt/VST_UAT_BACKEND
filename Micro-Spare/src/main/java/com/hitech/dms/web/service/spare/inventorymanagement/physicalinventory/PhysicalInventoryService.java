package com.hitech.dms.web.service.spare.inventorymanagement.physicalinventory;

import java.math.BigInteger;

import javax.transaction.Transactional;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory.PhysicalInventoryHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.StockAdjForPhyInvDto;

@Transactional
public interface PhysicalInventoryService {
	
	ApiResponse<?> getProductCatgry();
	
	ApiResponse<?> getPartsDetail(BigInteger branchId, BigInteger prodCatId, Boolean isZeroQty);
	
	ApiResponse<?> getToStores(BigInteger branchId);
	
	ApiResponse<?> getToBinLocation(BigInteger partBranchId, BigInteger storeId, String binLocation);
	
	ApiResponse<?> getAllNdp(BigInteger partBranchId, BigInteger storeId, BigInteger binId);
	
	ApiResponse<?> savePhysicalInventory(String userCode, StockAdjForPhyInvDto requestModel);
	
	ApiResponse<?> autoSearchPhyInvNo(String phyInvNo);
	
	ApiResponse<?>  searchPhysicalInventory(String userCode, SearchPhysicalInventoryRequestDto  requestModel);
	
	ApiResponse<?> viewPhysicalInventory(BigInteger phyInvId);
	

}
