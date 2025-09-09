/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.activity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
import com.hitech.dms.web.model.enquiry.activity.request.ActivityPlanListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityPlanListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPlanListDaoImpl implements ActivityPlanListDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityPlanListResponseModel> fetchEnqActivityPlanList(String userCode, Integer pcId,
			Integer activityId, Long dealerId, Date fieldActivityDate) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqActivityPlanList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ActivityPlanListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getActivityPlan] :userCode, :pcId, :activityId, :dealerId, :fieldActivityDate";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("activityId", activityId);
			query.setParameter("dealerId", dealerId);
			query.setParameter("fieldActivityDate", fieldActivityDate);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanListResponseModel>();
				ActivityPlanListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityPlanListResponseModel();
					responseModel.setActivityPlanID((BigInteger) row.get("activity_plan_id"));
					responseModel.setActivityPlanNumber((String) row.get("activity_number"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityPlanListResponseModel> fetchEnqActivityPlanList(String userCode,
			ActivityPlanListRequestModel planListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqActivityPlanList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ActivityPlanListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getActivityPlan] :userCode, :pcId, :activityId, :dealerId, :fieldActivityDate";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", planListRequestModel.getPcID());
			query.setParameter("activityId", planListRequestModel.getActivityID());
			query.setParameter("dealerId", planListRequestModel.getDealerID());
			query.setParameter("fieldActivityDate", (planListRequestModel.getFieldActivityDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(planListRequestModel.getFieldActivityDate())));
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanListResponseModel>();
				ActivityPlanListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityPlanListResponseModel();
					responseModel.setActivityPlanID((BigInteger) row.get("activity_plan_id"));
					responseModel.setActivityPlanNumber((String) row.get("activity_number"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
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
