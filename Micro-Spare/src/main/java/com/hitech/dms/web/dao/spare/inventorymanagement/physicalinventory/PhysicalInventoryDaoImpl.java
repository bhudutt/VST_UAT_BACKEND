package com.hitech.dms.web.dao.spare.inventorymanagement.physicalinventory;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryRequestDto;

@Repository
public class PhysicalInventoryDaoImpl implements PhysicalInventoryDao{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> getProductCatgry(Session session) {
		String sqlQuery = "EXEC [SP_GET_PRODUCT_CAT_LIST]";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> getPartsDetail(Session session, BigInteger branchId, BigInteger prodCatId, Boolean isZeroQty) {
		String sqlQuery = "EXEC [SP_Get_Parts_Detail]:branchId, :prodCatId, :isZeroQty";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("prodCatId", prodCatId);
	        query.setParameter("isZeroQty", isZeroQty);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> getToStores(Session session, BigInteger branchId) {
		String sqlQuery = "select branch_store_id toStoreId, storeDesc toStore from PA_BRANCH_STORE where branch_id = :branchId and IsActive = 'Y'";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
//	        query.setParameter("partBranchId", partBranchId);
//	        query.setParameter("storeId", storeId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> getToBinLocation(Session session, BigInteger partBranchId, BigInteger storeId, String binLocation) {
		String sqlQuery = "EXEC [SP_GET_BIN_DETAILS]:partBranchId, :storeId, :binLocation";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("storeId", storeId);
	        query.setParameter("binLocation", binLocation);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> getAllNdp(Session session, BigInteger partBranchId, BigInteger storeId, BigInteger binId) {
		String sqlQuery = "EXEC [SP_GET_NDP_DETAILS]:partBranchId, :storeId, :binId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("storeId", storeId);
	        query.setParameter("binId", binId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> autoSearchPhyInvNo(Session session, String phyInvNo) {
		String sqlQuery = "exec [SP_AUTO_SEARCH_PHY_INV_NO] :phyInvNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("phyInvNo", phyInvNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> searchPhysicalInventory(Session session, String userCode, SearchPhysicalInventoryRequestDto requestModel) {
	    String sqlQuery = "exec [SP_PHYSICAL_INVENTORY_SEARCH] :phyInvNo, :userCode, :status, :phyInvDoneBy, :productCategoryId, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("phyInvNo", requestModel.getPhyInvNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("status", requestModel.getStatus());
	        query.setParameter("phyInvDoneBy", requestModel.getPhyInvDoneBy());
	        query.setParameter("productCategoryId", requestModel.getProductCategoryId());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());

	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> viewPhysicalInventory(Session session, BigInteger phyInvId, Integer flag) {
		String sqlQuery = "exec [SP_PHYSICAL_INVENTORY_VIEW] :phyInvId, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("phyInvId", phyInvId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

}
