package com.hitech.dms.web.dao.goodsInTransitReport;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.goodsInTransitReport.request.GoodsInTransitReportRequest;

@Repository
public class GoodsInTransitReportDaoImpl implements GoodsInTransitReportDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<?> goodsInTransitSearch(String userCode, GoodsInTransitReportRequest request, Device device) {
		String sqlQuery = "exec [SP_Get_InTransitReport] :userCode, :stateId, :dealerId, :branchId, :asOnDate, :profitCenterId, :orgHierID, :modelId, :itemNumber, :includeInactive, :page, :size";

		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
		Query<?> query = null;
		List<?> data = new ArrayList<>();
		String formattedDateStr = dmyFormat.format(request.getAsOnDate());
		try {
			Session session = sessionFactory.getCurrentSession();
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("userCode", userCode);
			query.setParameter("stateId", request.getStateId());
			query.setParameter("dealerId", request.getDealerId());
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("asOnDate", formattedDateStr);
			query.setParameter("profitCenterId", request.getProfitCenterId());
			query.setParameter("orgHierID", request.getOrgHierID());
			query.setParameter("modelId", request.getModelId());
			query.setParameter("itemNumber", request.getItemNumber());
			query.setParameter("includeInactive", request.getIncludeInActive());
//			query.setParameter("zone", request.getZone());
//			query.setParameter("area", request.getArea());
//			query.setParameter("territory", request.getTerritory());
			query.setParameter("page", request.getPage());
			query.setParameter("size", request.getSize());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public List<?> itemDetailList(BigInteger modelId, String userCode) {
		  
		    Session session = null;
			NativeQuery<?> query = null;
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append("Select machine_item_id, model_id, item_no, item_description, variant from CM_MST_MACHINE_ITEM where model_id="+modelId);
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery.toString());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();

			return data;
		

	}
	
	
	

}
