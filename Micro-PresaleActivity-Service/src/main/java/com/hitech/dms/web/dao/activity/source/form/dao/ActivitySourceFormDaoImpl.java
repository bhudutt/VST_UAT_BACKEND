/**
 * 
 */
package com.hitech.dms.web.dao.activity.source.form.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.activityPlan.source.EnqActivityMstEntity;
import com.hitech.dms.web.entity.activityPlan.source.SourceMstEntity;
import com.hitech.dms.web.model.activity.source.form.request.ActivityFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.request.SourceFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.response.ActivitySourceFormResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivitySourceFormDaoImpl implements ActivitySourceFormDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceFormDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ActivitySourceFormResponseModel saveActivity(String userCode, ActivityFormRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveActivity invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		ActivitySourceFormResponseModel responseModel = new ActivitySourceFormResponseModel();
		EnqActivityMstEntity enqActivityMstEntity = null;
		String sqlQuery = "Select ActivityDesc from SA_MST_ENQ_SOURCE_ACTIVITY(nolock) sa where ActivityDesc =:activityDesc";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityDesc", requestModel.getActivityDescription());
			String existedActivityDesc = (String) query.getSingleResult();
			if (existedActivityDesc == null) {
				enqActivityMstEntity = new EnqActivityMstEntity();
				String code = "ACT/";
				enqActivityMstEntity.setActivityCode(code);
				enqActivityMstEntity.setActivityDescription(requestModel.getActivityDescription());
				enqActivityMstEntity.setCostPerDay(requestModel.getCostPerDay());
				enqActivityMstEntity.setGlCode(requestModel.getGlCode());
				enqActivityMstEntity.setIsActive(requestModel.getIsActive());
				enqActivityMstEntity.setCreatedBy(sqlQuery);
				enqActivityMstEntity.setCreatedDate(new Date());
				
				session.save(enqActivityMstEntity);
				
				int codeLength = Integer.toString(enqActivityMstEntity.getActivityId()).length();
				if (codeLength == 1) {
					code = code + "000" + enqActivityMstEntity.getActivityId();
				} else if (codeLength == 2) {
					code = code + "00" + enqActivityMstEntity.getActivityId();
				} else if (codeLength == 3) {
					code = code + "0" + enqActivityMstEntity.getActivityId();
				} else {
					code = code + enqActivityMstEntity.getActivityId();
				}
				enqActivityMstEntity.setActivityCode(code);
				session.merge(enqActivityMstEntity);
				
				transaction.commit();
				responseModel.setMsg("Activity Code Successfuly Added.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setMsg(
						"Activity Description Is Already Existed. Please Enter Different Activity Description.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActivitySourceFormResponseModel saveSource(String userCode, SourceFormRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveActivity invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		ActivitySourceFormResponseModel responseModel = new ActivitySourceFormResponseModel();
		SourceMstEntity sourceMstEntity = null;
		String sqlQuery = "Select SourceDescription from SA_MST_ENQ_SOURCE(nolock) so where SourceDescription =:sourceDescription and SourceCode =:sourceCode";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("sourceDescription", requestModel.getSourceDescription());
			query.setParameter("sourceCode", requestModel.getSourceCode());
			String existedActivityDesc = (String) query.getSingleResult();
			if (existedActivityDesc == null) {
				sourceMstEntity = new SourceMstEntity();
				sourceMstEntity.setApplicableForMobileApp(false);
				sourceMstEntity.setApplicableForWeb(true);
				sourceMstEntity.setIsActive(requestModel.getIsActive());
				sourceMstEntity.setIsSubSourceRequired(requestModel.getIsSubSourceRequired());
				sourceMstEntity.setSourceCode(requestModel.getSourceCode());
				sourceMstEntity.setSourceDescription(requestModel.getSourceDescription());
				
				sourceMstEntity.setCreatedBy(userCode);
				sourceMstEntity.setCreatedDate(new Date());
				session.save(sourceMstEntity);
				
				transaction.commit();
				responseModel.setMsg("Activity Code Successfuly Added.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setMsg(
						"Activity Description Is Already Existed. Please Enter Different Activity Description.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
}
