package com.hitech.dms.web.dao.deliverychallan;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCRequestDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchRequestDto;

@Repository
public class DeliveryChallanDaoImpl implements DeliveryChallanDao {

	@SuppressWarnings("deprecation")
	public List<?> wcrDispatchSearchList(Session session, String userCode, WcrDispatchRequestDto dispatchRequestDto) {
		String sqlQuery = "exec [WA_WCR_DISPATCH_LIST] :userCode, :wcrType, :fromDate, :toDate, :page, :size";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("userCode", userCode);
	        query.setParameter("wcrType", dispatchRequestDto.getWcrType());
	        query.setParameter("fromDate", dispatchRequestDto.getFromDate());
	        query.setParameter("toDate", dispatchRequestDto.getToDate());
	        query.setParameter("page", dispatchRequestDto.getPage());
	        query.setParameter("size", dispatchRequestDto.getSize());
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Map<String, Object>> wcrItemList(Session session, String wcrIds) {
		String sqlQuery = "exec [WA_DC_WCR_ITEM_LIST] :wcrIds";
		Query<?> query = null;
	    List<Map<String, Object>> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("wcrIds", wcrIds);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = (List<Map<String, Object>>) query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Map<String, Object>> fetchAllTranspoter(Session session) {
	    String sqlQuery = "select id, transporter as transporterName from SP_WA_MT_TRANSPORTER";
	    Query<?> query = null;
	    List<Map<String, Object>> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = (List<Map<String, Object>>) query.list();
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> autoSearchDcNo(Session session, String dcNo) {
		String sqlQuery = "exec [SV_WA_AUTO_SEARCH_DC_NO] :dcNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("dcNo", dcNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> autoSearchLrNo(Session session, String lrNo) {
		String sqlQuery = "exec [SV_WA_AUTO_SEARCH_LR_NO] :lrNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("lrNo", lrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@SuppressWarnings("deprecation")
	public List<?> warrantyPartsDCSearchList(Session session, String userCode, WarrantyPartsDCRequestDto requestModel) {
		String sqlQuery = "exec [SV_WA_PART_DC_SEARCH_LIST] :dcNo, :userCode, :lrNo, :transporterName, :fromDate, :toDate, :page, :size";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("dcNo", requestModel.getDcNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("lrNo", requestModel.getLrNo());
	        query.setParameter("transporterName", requestModel.getTransporterName());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> viewDcList(Session session, BigInteger id, Integer flag) {
		String sqlQuery = "exec [SV_WARRANTY_PART_DC_VIEW] :id, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("id", id);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

}
