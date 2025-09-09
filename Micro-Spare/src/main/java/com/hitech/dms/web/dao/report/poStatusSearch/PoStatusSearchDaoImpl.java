package com.hitech.dms.web.dao.report.poStatusSearch;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.report.model.PoStatusSearchRequest;

@Repository
@Transactional
public class PoStatusSearchDaoImpl implements PoStatusSearchDao{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> autoCompletePoNo(Session session, String poNo, BigInteger branchId) {
	    String sqlQuery = "exec [PA_AUTOCOMPLETE_PO_NO] :branchId, :poNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("branchId", branchId);
	        query.setParameter("poNo", poNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public List<?> poStatusReportSearch(Session session, String userCode, PoStatusSearchRequest resquest, Device device) {
		
		Query query = null;
		List<?> data = null;
		boolean isSuccess = true;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String[] pa =null;
			if(resquest.getPartNumber()!=null) {
			 pa = resquest.getPartNumber().split("\\|");
			}
			String sqlQuery = "exec [SP_PO_STATUS_REPORT]  :userCode, :branchId, :poNo, :partNo, :fromDate, :toDate, :page, :size";
			
			query = session.createSQLQuery(sqlQuery);

			
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", resquest.getBranchId());
			query.setParameter("poNo", resquest.getPoNumber());
			query.setParameter("partNo", pa!=null?pa[0].trim():null);
			query.setParameter("fromDate", resquest.getFromDate());
			query.setParameter("toDate", resquest.getToDate());
			query.setParameter("page", resquest.getPage());
			query.setParameter("size", resquest.getSize());
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			data = query.list();

		} catch (Exception e) {
		     e.printStackTrace();
		}
		return data;
	}

}
