/**
 * 
 */
package com.hitech.dms.web.dao.activity.search;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.activity.search.request.ActualActivitySearchRequestModel;
import com.hitech.dms.web.model.activity.search.response.ActualActivitySearchResponse;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActualActivitySearchDaoImpl implements ActualActivitySearchDao {
	private static final Logger logger = LoggerFactory.getLogger(ActualActivitySearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActualActivitySearchResponse> fetchActualActivitySearchList(String userCode,
			ActualActivitySearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"fetchActualActivitySearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActualActivitySearchResponse> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_SEARCH_ACTUALACTIVITIES] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :actualActivityNumber, :fromDate, :toDate, :isIncludeInActive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("orgHierId", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("actualActivityNumber", requestModel.getActualActivityNumber());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("isIncludeInActive", requestModel.getIncludeInActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActualActivitySearchResponse>();
				ActualActivitySearchResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActualActivitySearchResponse();
					responseModel.setSrlNo((BigInteger) row.get("Sr_No"));
					responseModel.setActivityActualHdrId((BigInteger) row.get("ActivityActualHdrId"));
					responseModel.setActivityDesc((String) row.get("ActivityDesc"));
					responseModel.setActivityActualNo((String) row.get("ActivityActualNo"));
					responseModel.setActivityActualDate((String) row.get("ActivityActualDate"));
					responseModel.setActivityActualStatus((String) row.get("ActivityActualStatus"));
					responseModel.setActivityActualFromDate((String) row.get("ActivityActualFromDate"));
					responseModel.setActivityActualToDate((String) row.get("ActivityActualToDate"));
					responseModel.setActivityLocation((String) row.get("ActivityLocation"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
}
