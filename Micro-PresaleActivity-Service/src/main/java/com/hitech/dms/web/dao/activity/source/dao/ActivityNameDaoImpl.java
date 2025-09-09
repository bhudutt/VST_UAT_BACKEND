/**
 * 
 */
package com.hitech.dms.web.dao.activity.source.dao;

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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityNameDaoImpl implements ActivityNameDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityNameDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivitySourceListResponseModel> fetchActivityNameListBasedOnActivityPlanId(String userCode,
			BigInteger activityPlanHDRId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityNameListBasedOnActivityPlanId invoked..");
		}
		Session session = null;
		Query query = null;
		List<ActivitySourceListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYNAME_LIST] :userCode, :activityPlanHDRId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("activityPlanHDRId", activityPlanHDRId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivitySourceListResponseModel>();
				ActivitySourceListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivitySourceListResponseModel();
					responseModel.setActivityId((Integer) row.get("Activity_ID"));
					responseModel.setActivityCode((String) row.get("ActivityCode"));
					responseModel.setActivityDesc((String) row.get("ActivityName"));

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
