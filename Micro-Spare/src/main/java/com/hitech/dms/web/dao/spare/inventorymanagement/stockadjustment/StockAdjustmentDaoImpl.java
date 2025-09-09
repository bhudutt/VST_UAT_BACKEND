package com.hitech.dms.web.dao.spare.inventorymanagement.stockadjustment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.ApprovalAdjPartDetail;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.SearchStockAdjustmentRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentApprovalRequestDto;

@Repository
@SuppressWarnings("deprecation")
public class StockAdjustmentDaoImpl implements StockAdjustmentDao {
	
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public List<?> autoSearchAdjustmentNo(Session session, String adjustmentNo) {
		String sqlQuery = "exec [SP_AUTO_SEARCH_ADJUSTMENT_NO] :adjustmentNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("adjustmentNo", adjustmentNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> searchStockAdjustment(Session session, String userCode, SearchStockAdjustmentRequestDto requestModel) {
	    String sqlQuery = "exec [SP_STOCK_ADJUSTMENT_SEARCH] :adjustmentNo, :userCode, :hoUserId, :status, :adjustmentDoneBy, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;
	    Map<String, Object> mapData = null;

	    try {
	    	mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
	    	BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("adjustmentNo", requestModel.getAdjustmentNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("hoUserId", hoUserId);
	        query.setParameter("status", requestModel.getStatus());
	        query.setParameter("adjustmentDoneBy", requestModel.getAdjustmentDoneBy());
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
	public List<?> viewStockAdjustment(Session session, BigInteger adjustmentId, Integer flag) {
		String sqlQuery = "exec [SP_STOCK_ADJUSTMENT_VIEW] :adjustmentId, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("adjustmentId", adjustmentId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> approveRejectStockAdj(Session session, String userCode, BigInteger hoUserId, StockAdjustmentApprovalRequestDto requestModel) {
	    String sqlQuery = "exec SP_STOCK_ADJUSTMENT_APPROVALS :hoUserId, :adjustmentId, :approvalStatus, :remark, :rejectReason";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hoUserId", hoUserId);
			query.setParameter("adjustmentId", requestModel.getAdjustmentId());
			query.setParameter("approvalStatus", requestModel.getApprovalStatus());
			query.setParameter("remark", requestModel.getRemarks());
			query.setParameter("rejectReason", requestModel.getRejectReason());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> checkIfPartOrMrpExist(Session session, BigInteger branchId, String storeName, String binName,
			String partNo, BigDecimal partMrp, String adjustmentType) {
		String sqlQuery = "exec [SP_CHECK_PART_MRP_EXIST] :branchId, :storeName, :binName, :partNo, :partMrp, :adjustmentType";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("storeName", storeName);
	        query.setParameter("binName", binName);
	        query.setParameter("partNo", partNo);
	        query.setParameter("partMrp", partMrp);
	        query.setParameter("adjustmentType", adjustmentType);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return data;
	}

	@Override
	public List<?> getApprovalHierarchy(Session session, boolean dealerFlag) {
		String sqlQuery = "exec [SP_STOCK_ADJUSTMENT_APPROVAL_HIERARCHY_LEVEL] :dealerFlag";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("dealerFlag", dealerFlag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> getMrplist(Session session, BigInteger partId) {
		String sqlQuery = "EXEC [SP_GET_MRP_LIST] :partId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("partId", partId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> updatePartMrp(Session session, BigInteger adjustmentId) {
		String sqlQuery = "exec [SP_PART_MRP_ADJUSTMENT] :adjustmentId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("adjustmentId", adjustmentId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId) {
	    String sqlQuery = "exec [SP_AUTOCOMPLETE_PART_NUMBER_FOR_STK_ADJ] :partNo, :branchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("partNo", partNo);
	        query.setParameter("branchId", branchId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> getStorelist(Session session, BigInteger partBranchId, BigInteger branchId) {
		String sqlQuery = "EXEC [SP_GET_STORE_LIST_FOR_STK_ADJ] :partBranchId, :branchId";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        if (partBranchId == null) {
	            query.setParameter("partBranchId", 0);
	        } else {
	            query.setParameter("partBranchId", partBranchId);
	        }
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	
	@Override
	public void insertDataFromStoreTemptoUpload(Session session, BigInteger adjustmentId) {
	    String sqlQuery = "exec [SP_InsertDataFromStoreTemptoUpload] :adjustmentId";

	    Query<?> query = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("adjustmentId", adjustmentId);
	        query.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	@Override
	public void uploadStockDataToMain(Session session) {
	    String sqlQuery = "exec [UploadStockData]";

	    Query<?> query = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void uploadStockAdjustmentDetail(Session session,List<ApprovalAdjPartDetail> requestModel) {
		
		  String sqlQuery = "exec [SP_UPDATE_STOCKADJUSTMENT_DTL] :adjustmentdtlId, :branch_id, :part_id, :store_id";

		    Query<?> query = null;
		    
		    try {
		    	for(ApprovalAdjPartDetail bean:requestModel) {
		    		
		        query = session.createSQLQuery(sqlQuery);
		        query.setParameter("adjustmentdtlId", bean.getAdjustmentDtlId());
		        query.setParameter("branch_id", bean.getBranchId());
		        query.setParameter("part_id", bean.getPartId());
		        query.setParameter("store_id", bean.getStoreId());
		        query.executeUpdate();
		    	}
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		
	}


}
