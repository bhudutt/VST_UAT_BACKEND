package com.hitech.dms.web.dao.activitymaster.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.activitymaster.ActivitySourceMasterEntity;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceCostTypeResponseModel;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceMasterListResponseModel;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceMasterResponseModel;


@Repository
public class ActivitySourceMasterDaoImpl implements ActivitySourceMasterDao{

	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceMasterDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Override
	public ActivitySourceMasterResponseModel createActivityMaster(String userCode, Device device,
			ActivitySourceMasterEntity requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("activitySourceMasterEntity invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		ActivitySourceMasterResponseModel responseModel = new ActivitySourceMasterResponseModel();
		Query query = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		session.save(requestModel);
		
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Activity Master Created Successfully";
			responseModel.setMsg(msg);
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
	
      }
  return responseModel;
	}
	
	@Override
	public List<ActivitySourceCostTypeResponseModel> fetchActivityCostType(String userCode, Device device) {
		
		Session session = null;
		List<ActivitySourceCostTypeResponseModel> responseModelList = null;
		ActivitySourceCostTypeResponseModel responseModel = null;
		String costType="COST_TYPE";
		Query<ActivitySourceCostTypeResponseModel> query = null;
		
		String sqlQuery = "select lookup_id, LookupTypeCode, LookupVal, DisplayOrder  from SYS_LOOKUP where LookupTypeCode= '"+costType+"'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivitySourceCostTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivitySourceCostTypeResponseModel();
					responseModel.setCostType((String) row.get("LookupVal"));
					responseModel.setLookupTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setOrderdisplay((Integer) row.get("DisplayOrder"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;

	}
	@Override
	public ActivitySourceMasterResponseModel changeActiveStatus(String userCode, Integer activityId,
			Character isActive) {
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		ActivitySourceMasterResponseModel responseModel = new ActivitySourceMasterResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query1 = session.createQuery("UPDATE ActivitySourceMasterEntity SET IsActive = :isActive WHERE Activity_ID = :activityId");
			query1.setParameter("isActive", isActive);
			query1.setParameter("activityId", activityId);
			int activityMasterEntity = query1.executeUpdate();
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			
			msg = "Active Status Update Successfully";
			responseModel.setMsg(msg);
		}else {
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			msg = "Bad Request";
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
	
      }
     return responseModel;
	}
	@Override
	public List<ActivitySourceMasterListResponseModel> fetchActivitySourceMasterList(String userCode) {
		Session session = null;
		List<ActivitySourceMasterListResponseModel> responseModelList = null;
		ActivitySourceMasterListResponseModel responseModel = null;
		
		Query<ActivitySourceMasterListResponseModel> query = null;
		
		String sqlQuery = "exec [SP_GET_ACTIVITYSOURCE_MASTER]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivitySourceMasterListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivitySourceMasterListResponseModel();
					responseModel.setId((Integer) row.get("Activity_id"));
					responseModel.setActivityType((String) row.get("ActivityType"));
					responseModel.setProfitCenter((String) row.get("pc_desc"));
					responseModel.setGlCode((String) row.get("GlCode"));
					responseModel.setActivitySourceName((String) row.get("ActivitySourceName"));
					responseModel.setActivityCostPerDay((String) row.get("ActivityCostPerDay"));
					responseModel.setActive((Character) row.get("IsActive"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;

	}

	@Override
	public List<ActivitySourceMasterResponseModel> fetchActivityNameList(String userCode, String searchFor,
			String activityName) {
		
		Session session = null;
		List<ActivitySourceMasterResponseModel> responseList = null;
		ActivitySourceMasterResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETACTIVITY_SOURCEMASTER] :searchFor, :ActivityName";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchFor", searchFor);
			query.setParameter("ActivityName", activityName);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ActivitySourceMasterResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ActivitySourceMasterResponseModel();
					response.setMsg((String) row.get("Message"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

}
