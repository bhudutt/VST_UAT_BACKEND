package com.hitech.dms.web.dao.spare.inventorymanagement.physicalinventory;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryRequestDto;

@Transactional
public interface PhysicalInventoryDao {
	
	List<?> getProductCatgry(Session session);
	
	List<?> getPartsDetail(Session session, BigInteger branchId, BigInteger prodCatId, Boolean isZeroQty);
	
	List<?> getToStores(Session session, BigInteger branchId);
	
	List<?> getToBinLocation(Session session, BigInteger branchId, BigInteger storeId, String binLocation);
	
	List<?> getAllNdp(Session session, BigInteger partBranchId, BigInteger storeId, BigInteger binId);
	
	List<?> autoSearchPhyInvNo(Session session, String phyInvNo);
	
	List<?> searchPhysicalInventory(Session session, String userCode, SearchPhysicalInventoryRequestDto requestModel);
	
	List<?> viewPhysicalInventory(Session session, BigInteger phyInvId, Integer flag);

}
