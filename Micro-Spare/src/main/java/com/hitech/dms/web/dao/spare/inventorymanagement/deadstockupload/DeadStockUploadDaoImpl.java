package com.hitech.dms.web.dao.spare.inventorymanagement.deadstockupload;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockSearchRequest;

@Repository
public class DeadStockUploadDaoImpl implements DeadStockUploadDao{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> uploadDeadStock(Session session, BigInteger branchId, String partNo) {
		String sqlQuery = "exec [SP_DEAD_STOCK_UPLOAD] :branchId, :partNo";

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
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId) {
	    String sqlQuery = "exec [SP_AUTOSEARCH_PART_NUMBER] :branchId, :partNo";

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
	
	
	@SuppressWarnings("deprecation")
	public List<?> searchDeadStockUpload(Session session, String userCode, DeadStockSearchRequest requestModel) {
		String sqlQuery = "exec [SP_DEAD_STOCK_UPLOAD_SEARCH] :userCode, :partId, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("userCode", userCode);
	        query.setParameter("partId", requestModel.getPartId());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return data;
	}

}
