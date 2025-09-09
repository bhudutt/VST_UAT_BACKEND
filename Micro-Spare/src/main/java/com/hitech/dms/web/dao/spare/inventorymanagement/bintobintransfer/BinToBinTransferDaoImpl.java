package com.hitech.dms.web.dao.spare.inventorymanagement.bintobintransfer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferRequestDto;

@Repository
@SuppressWarnings("deprecation")
public class BinToBinTransferDaoImpl implements BinToBinTransferDao {
	
	@Override
	public List<?> getSpareEmployee(Session session, BigInteger branchId) {
		String sqlQuery = "EXEC [SP_GET_SPARE_EMPLOYEE] :branchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId) {
	    String sqlQuery = "exec [SP_AUTOCOMPLETE_PART_NUMBER] :branchId, :partNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("partNo", partNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> getStorelist(Session session, BigInteger partBranchId, BigInteger branchId) {
		String sqlQuery = "EXEC [SP_GET_STORE_LIST] :partBranchId, :branchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("partBranchId", partBranchId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> getStoreBinlist(Session session, String binName, BigInteger branchId, BigInteger stockStoreId, BigInteger partBranchId) {
		String sqlQuery = "EXEC [SP_GET_STORE_BIN_LIST]:binName, :branchId, :stockStoreId, :partBranchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("binName", binName);
	        query.setParameter("branchId", branchId);
	        query.setParameter("stockStoreId", stockStoreId);
	        query.setParameter("partBranchId", partBranchId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> autoSearchIssueNo(Session session, String issueNo) {
		String sqlQuery = "exec [SP_AUTO_SEARCH_ISSUE_NO] :issueNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("issueNo", issueNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> autoSearchReceiptNo(Session session, String receiptNo) {
		String sqlQuery = "exec [SP_AUTO_SEARCH_RECEIPT_NO] :receiptNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("receiptNo", receiptNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> searchBinToBinTransfer(Session session, String userCode, SearchBinToBinTransferRequestDto requestModel) {
	    String sqlQuery = "exec [SP_BIN_TO_BIN_TRANSFER_SEARCH] :issueNo, :userCode, :receiptNo, :transferDoneBy, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("issueNo", requestModel.getIssueNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("receiptNo", requestModel.getReceiptNo());
	        query.setParameter("transferDoneBy", requestModel.getTransferDoneBy());
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
	
	@Override
	public List<?> viewBinToBinTransfer(Session session, BigInteger issueId, Integer flag) {
		String sqlQuery = "exec [SP_BIN_TO_BIN_TRANSFER_VIEW] :issueId, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("issueId", issueId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> createBin(Session session, BigInteger branchId, BigInteger partBranchId, BigInteger storeId,
			String binName, String createdBy) {		
		String sqlQuery = "EXEC [sp_create_bin] :branchId, :partBranchId, :storeId, :binName, :createdBy";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("storeId", storeId);
	        query.setParameter("binName", binName);
	        query.setParameter("createdBy", createdBy);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public String updateStock(Session session, String flag, BigInteger branchId, BigInteger partBranchId,
			BigInteger stockStoreId, BigInteger stockBinId, BigDecimal qtyToBeAdded, BigDecimal qtyToBeSubtracted,
			BigDecimal basicUnitPrice, String tableName, String modifiedBy) {
		String sqlQuery = "EXEC [SP_Update_Bin_Stock] :flag, :branchId, :partBranchId, :stockStoreId,"
				+ " :stockBinId, :qtyToBeAdded, :qtyToBeSubtracted, :basicUnitPrice, :tableName, :modifiedBy";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("flag", flag);
	        query.setParameter("branchId", branchId);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("stockStoreId", stockStoreId);
	        query.setParameter("stockBinId", stockBinId);
	        query.setParameter("qtyToBeAdded", qtyToBeAdded);
	        query.setParameter("qtyToBeSubtracted", qtyToBeSubtracted);
	        query.setParameter("basicUnitPrice", basicUnitPrice);
	        query.setParameter("tableName", tableName);
	        query.setParameter("modifiedBy", modifiedBy);
	        
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    @SuppressWarnings("rawtypes")
		Map row = (Map) data.get(0);
	    
	    String msg = (String) row.get("message");
	    
	    return msg;
	}

	@Override
	public BigDecimal getBasicUnitPrice(Session session, String userCode, BigInteger branchId, BigInteger partBranchId) {
		String sqlQuery = "EXEC [SP_Get_Part_Details] :partBranchId, :UserCode, :branchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("UserCode", userCode);
	        query.setParameter("branchId", branchId);
	        
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    @SuppressWarnings("rawtypes")
		Map row = (Map) data.get(0);
	    
	    BigDecimal basicUnitPrice = (BigDecimal) row.get("basicUnitPrice");
	    
	    return basicUnitPrice;
	}
	
	@Override
	public List<?> getStockStores(Session session, BigInteger branchId, BigInteger partBranchId, BigInteger storeId) {
		String sqlQuery = "EXEC [SP_GET_STOCK_STORE_LIST]:branchId, :partBranchId, :storeId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("partBranchId", partBranchId);
	        query.setParameter("storeId", storeId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

}
