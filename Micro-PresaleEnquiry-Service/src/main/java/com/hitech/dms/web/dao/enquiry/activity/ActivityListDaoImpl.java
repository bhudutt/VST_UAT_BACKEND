/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.activity;

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

import com.hitech.dms.web.model.enquiry.activity.request.ActivityListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityListDaoImpl implements ActivityListDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityListResponseModel> fetchEnqActivityList(String userCode, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqActivityList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ActivityListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_getActivityList] :pcId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityListResponseModel>();
				ActivityListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityListResponseModel();
					responseModel.setActivityId((Integer) row.get("Activity_ID"));
					responseModel.setActivityCode((String) row.get("ActivityCode"));
					responseModel.setActivityName((String) row.get("ActivityName"));
					responseModel.setGlCode((String) row.get("GL_CODE"));
					responseModel.setCostPerDay((Double) row.get("COST_PER_DAY"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityListResponseModel> fetchEnqActivityList(String userCode,
			ActivityListRequestModel activityListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqActivityList invoked.." + activityListRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActivityListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_getActivityList] :pcId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", activityListRequestModel.getPcId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityListResponseModel>();
				ActivityListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityListResponseModel();
					responseModel.setActivityId((Integer) row.get("Activity_ID"));
					responseModel.setActivityCode((String) row.get("ActivityCode"));
					responseModel.setActivityName((String) row.get("ActivityName"));
					responseModel.setGlCode((String) row.get("GL_CODE"));
					responseModel.setCostPerDay((Double) row.get("COST_PER_DAY"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
}
