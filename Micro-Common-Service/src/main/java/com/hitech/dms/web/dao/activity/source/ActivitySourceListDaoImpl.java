/**
 * 
 */
package com.hitech.dms.web.dao.activity.source;

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

import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;
import com.hitech.dms.web.model.activity.source.response.SourceListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivitySourceListDaoImpl implements ActivitySourceListDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<ActivitySourceListResponseModel> fetchActivitySourceListByPcId(String userCode, Integer pcId,
			String isFor, String isIncludeInActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceListByPcId invoked.." + userCode);
		}
		Session session = null;
		List<ActivitySourceListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchActivitySourceListByPcId(session, userCode, pcId, isFor, isIncludeInActive);
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
	public List<ActivitySourceListResponseModel> fetchActivitySourceListByPcId(Session session, String userCode,
			Integer pcId, String isFor, String isIncludeInActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceListByPcId invoked.." + userCode + " " + pcId);
		}
		Query query = null;
		List<ActivitySourceListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_Activity_getSourceActivityList] :userCode, :pcId, :isFor, :isIncludeInActive";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("isFor", isFor);
			query.setParameter("isIncludeInActive", isIncludeInActive);
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
					responseModel.setActivityDesc((String) row.get("ActivityDesc"));
					responseModel.setGlCode((String) row.get("GL_CODE"));
					responseModel.setCostPerDay((Double) row.get("COST_PER_DAY"));
					Character isActive = (Character) row.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(true);
					} else {
						responseModel.setIsActive(false);
					}
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
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SourceListResponseModel> fetchSourceList(Session session, String userCode, Integer pcId, String isFor,
			String isIncludeInActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSourceList invoked.." + userCode + " " + pcId);
		}
		Query query = null;
		List<SourceListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_Activity_getSourceActivityList] :userCode, :pcId, :isFor, :isIncludeInActive";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("isFor", isFor);
			query.setParameter("isIncludeInActive", isIncludeInActive);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SourceListResponseModel>();
				SourceListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SourceListResponseModel();
					responseModel.setEnqSourceId((Integer) row.get("Enq_Source_Id"));
					responseModel.setSourceCode((String) row.get("SourceCode"));
					responseModel.setSourceDesccription((String) row.get("SourceDescription"));
					Character isActive = (Character) row.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(true);
					} else {
						responseModel.setIsActive(false);
					}
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
		}
		return responseModelList;
	}

	public List<ActivitySourceListResponseModel> fetchActivitySourceListByPcId(
			ActivitySourceListRequestModel activitySourceListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceListByPcId invoked.." + activitySourceListRequestModel.toString());
		}
		Session session = null;
		List<ActivitySourceListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchActivitySourceListByPcId(session, activitySourceListRequestModel.getUserCode(),
					activitySourceListRequestModel.getPcId(), activitySourceListRequestModel.getIsFor(),
					activitySourceListRequestModel.getIsIncludeInActive());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	public List<SourceListResponseModel> fetchSourceList(String userCode, Integer pcId,
			String isFor, String isIncludeInActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSourceList invoked.." + userCode);
		}
		Session session = null;
		List<SourceListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchSourceList(session, userCode, pcId, isFor, isIncludeInActive);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<SourceListResponseModel> fetchSourceList(
			ActivitySourceListRequestModel activitySourceListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSourceList invoked.." + activitySourceListRequestModel.toString());
		}
		Session session = null;
		List<SourceListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchSourceList(session, activitySourceListRequestModel.getUserCode(),
					activitySourceListRequestModel.getPcId(), activitySourceListRequestModel.getIsFor(),
					activitySourceListRequestModel.getIsIncludeInActive());
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
