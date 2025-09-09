package com.hitech.dms.web.dao.goodwill;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.query.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;

@Repository
@SuppressWarnings("deprecation")
public class GoodwillDaoImpl implements GoodwillDao {
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public List<?> getApprovalHierarchy(Session session){
	    
		String sqlQuery = "exec [sp_wa_pcr_get_approval_hierarchy_level]";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
	
	
	@Override
	public List<?> autoSearchGoodwillNo(Session session, String goodwillNo) {
	    String sqlQuery = "exec [sv_wa_goodwill_autosearch_goodwill_No] :goodwillNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("goodwillNo", goodwillNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> autoSearchJcNo(Session session, String roNo) {
	    String sqlQuery = "exec [sv_wa_pcr_autosearch_jobcard_No_for_goodwill] :roNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roNo", roNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> autoSearchPcrNo(Session session, String pcrNo) {
	    String sqlQuery = "exec [sv_wa_goodwill_autosearch_pcr_No] :pcrNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("pcrNo", pcrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> autoSearchChassisNo(Session session, String chassisNo) {
	    String sqlQuery = "exec [sv_wa_goodwill_autosearch_chassis_No] :chassisNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("chassisNo", chassisNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	
	@Override
	public List<?> goodwillSearchList(Session session, String userCode, GoodwillSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_WA_Goodwill_Search_Details] :GoodWillNo, :userCode, :hoUserId, :PCRNO, :RONumber, :ChassisNo, :Status, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;
	    Map<String, Object> mapData = null;
	    @SuppressWarnings("unused")
		boolean isSuccess = true;

	    try {
	        mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
//	        if (mapData != null && mapData.get("SUCCESS") != null) {
	        	BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
	        	query = session.createSQLQuery(sqlQuery);
		        query.setParameter("GoodWillNo", requestModel.getGoodwillNo());
		        query.setParameter("userCode", userCode);
		        query.setParameter("hoUserId", hoUserId);
		        query.setParameter("PCRNO", requestModel.getPcrNo());
		        query.setParameter("RONumber", requestModel.getJobCardNo());
		        query.setParameter("ChassisNo", requestModel.getChassisNo());
		        query.setParameter("Status", requestModel.getStatus());
		        query.setParameter("fromDate", requestModel.getFromDate());
		        query.setParameter("toDate", requestModel.getToDate());
		        query.setParameter("page", requestModel.getPage());
		        query.setParameter("size", requestModel.getSize());
	
		        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		        data = query.list();
//	        }else {
//				isSuccess = false;
////				mapData.setMsg("User Not Found.");
//			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	
	@SuppressWarnings("unused")
	@Override
	public List<?> approveRejectGoodwill(Session session, String userCode, GoodwillApprovalRequestDto requestModel) {
	    String sqlQuery = "exec [SV_WA_GOODWILL_APPROVAL] :hoUserId, :goodwillId, :approvalStatus, :remark, :rejectReason";

	    Map<String, Object> mapData = null;
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	    	mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
	    	if (mapData != null && mapData.get("SUCCESS") != null) {
	    		BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
		    	query = session.createNativeQuery(sqlQuery);
				query.setParameter("hoUserId", hoUserId);
				query.setParameter("goodwillId", requestModel.getGoodwillId());
				query.setParameter("approvalStatus", requestModel.getApprovalStatus());
				query.setParameter("remark", requestModel.getRemarks());
				query.setParameter("rejectReason", requestModel.getRejectReason());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	    	}
		    data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> getViewData(Session session, BigInteger goodwillId, int flag) {
		
		String sqlQuery = "exec [SP_SV_WA_GOODWILL_VIEW] :goodwillId, :flag";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("goodwillId", goodwillId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
}
