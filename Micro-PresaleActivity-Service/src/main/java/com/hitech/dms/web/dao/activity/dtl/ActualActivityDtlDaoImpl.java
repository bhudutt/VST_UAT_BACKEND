/**
 * 
 */
package com.hitech.dms.web.dao.activity.dtl;

import java.math.BigInteger;
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

import com.hitech.dms.web.model.activity.dtl.request.ActualActivityDTLRequestModel;
import com.hitech.dms.web.model.activity.dtl.response.ActualActivityDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActualActivityDtlDaoImpl implements ActualActivityDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(ActualActivityDtlDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActualActivityDTLResponseModel fetchActivityPlanHDRDTL(String userCode,
			ActualActivityDTLRequestModel requestModel) {
		Session session = null;
		Query query = null;
		ActualActivityDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SACT_ACTUALACTIVITY_HDR_DTL] :userCode, :actualActivityHdrId";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("actualActivityHdrId", requestModel.getActualActivityHdrId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModel = new ActualActivityDTLResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setActivityPlanHdrId((BigInteger) row.get("activity_plan_hdr_id"));
					responseModel.setActivityNo((String) row.get("activityNo"));
					responseModel.setActivityCreationDate((String) row.get("activity_creation_date"));
					responseModel.setActivityMonthNo((Integer) row.get("activity_month"));
					responseModel.setActivityMonth((String) row.get("activityMonth"));
					responseModel.setActivityYear((Integer) row.get("activity_year"));
					
					responseModel.setActualActivityNo((String) row.get("ActualActivityNo"));
					responseModel.setActivityActualFromDate((String) row.get("ActivityActualFromDate"));
					responseModel.setActivityActualStatus((String) row.get("ActivityActualStatus"));
					responseModel.setActivityActualToDate((String) row.get("ActivityActualToDate"));
					responseModel.setActivityCreationDate((String) row.get("ActivityCreationDate"));
					responseModel.setActivityId((Integer) row.get("ActivityId"));
					responseModel.setActivityName((String) row.get("ActivityName"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setActivityLocation((String) row.get("ActivityLocation"));
					responseModel.setPcId((Integer) row.get("pc_id"));
					responseModel.setPcDesc((String) row.get("profit_center"));
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
