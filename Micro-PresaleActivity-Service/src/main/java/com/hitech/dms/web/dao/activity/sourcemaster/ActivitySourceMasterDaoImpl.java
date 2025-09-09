package com.hitech.dms.web.dao.activity.sourcemaster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.hitech.dms.web.entity.activity.sourcemaster.ActivitySourceMasterEntity;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivityGlCodeListModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterIsExist;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterListResponseModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterResponseModel;


@Repository
public class ActivitySourceMasterDaoImpl implements ActivitySourceMasterDao {

	
	private static final Logger logger = LoggerFactory.getLogger(ActivitySourceMasterDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ActivitySourceMasterResponseModel createActivitySourceMaster(String userCode,
			ActivitySourceMasterEntity activitySourceMasterEntity, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("activitySourceMasterEntity invoked.." + userCode);
			logger.debug(activitySourceMasterEntity.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		ActivitySourceMasterResponseModel responseModel = new ActivitySourceMasterResponseModel();
		Query query = null;
		boolean isSuccess = true;
		boolean isExist =false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String finalValue=null;
			ActivitySourceMasterIsExist isAlreadyExist = activityNameExist(session,userCode,activitySourceMasterEntity);
			if(isAlreadyExist==null) {
				isExist = true;
			}
				
			if(isAlreadyExist!=null && isAlreadyExist.getTotalCount()>0
			  && !isAlreadyExist.getActivityType().equalsIgnoreCase(activitySourceMasterEntity.getActivityType())
			  && isAlreadyExist.getCostPerDay()!=activitySourceMasterEntity.getActivityCostPerDay())
			{
				isExist = true;
				activitySourceMasterEntity.setActivityCostPerDay(activitySourceMasterEntity.getActivityCostPerDay());
				activitySourceMasterEntity.setActivityType(activitySourceMasterEntity.getActivityType());
			}	
			if(isExist) {
			Map<String, String> data= fetchActivityCodeIncrement(session);
			for(String value : data.values()) {
				finalValue=value;
				System.out.println(value);
			}
			activitySourceMasterEntity.setActivityCode(finalValue);
			activitySourceMasterEntity.setCreatedBy(userCode);
		    session.saveOrUpdate(activitySourceMasterEntity);
		
				if (isSuccess) {
					transaction.commit();
					session.close();
					//sessionFactory.close();
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					msg = "Activity Master Created Successfully";
					responseModel.setMsg(msg);
				}
			}else {
				responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
				msg = "Activity Name Already For This Profit Center.";
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

	private ActivitySourceMasterIsExist activityNameExist(Session session, String userCode,
			ActivitySourceMasterEntity activitySourceMasterEntity) {
		ActivitySourceMasterIsExist bean =null;
		Query query = null;
		Boolean flag = false;
		String sqlQuery = "EXEC [IS_EXIST_ACTIVITY_NAME_AND_PC] :pcId,:activityName,:userCode";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", activitySourceMasterEntity.getProfitCenter());
			query.setParameter("activityName", activitySourceMasterEntity.getActivityName());
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				bean =new ActivitySourceMasterIsExist();
				Map row = (Map) data.get(0);
				bean.setTotalCount((Integer)row.get("totalRecord"));
				bean.setCostPerDay(BigDecimal.valueOf((Double )row.get("COST_PER_DAY")));
				bean.setActivityType((String)row.get("ActivityType"));
				
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
		return bean;
	}

	@Override
	public List<ActivitySourceMasterListResponseModel> fetchActivitySourceMasterList(String userCode,
			Integer Activity_ID) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceMasterList invoked.." + userCode);
			

		}
		Session session = null;
		
		List<ActivitySourceMasterListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchActivitySourceMasterList(session, userCode, Activity_ID);
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
	public List<ActivitySourceMasterListResponseModel> fetchActivitySourceMasterList(Session session, String userCode,
			Integer Activity_ID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceMasterList invoked.." + userCode + " ");
			
		}
		Query query = null;
		List<ActivitySourceMasterListResponseModel> responseModelList = null;
		String sqlQuery = "select MST.activity_id, MST.ActivityType, PRO.pc_desc, PRO.pc_id, MST.ActivityCode, MST.Gl_Code as GlCode, MST.ActivityDesc as ActivitySourceName, MST.COST_PER_DAY as ActivityCostPerDay, MST.IsActive from SA_MST_ENQ_SOURCE_ACTIVITY MST join ADM_BP_MST_PROFIT_CENTER PRO on PRO.pc_id=MST.pc_id";
		try {
			query = session.createSQLQuery(sqlQuery);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivitySourceMasterListResponseModel>();
				ActivitySourceMasterListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivitySourceMasterListResponseModel();
					responseModel.setActivityType((String) row.get("ActivityType"));
					responseModel.setActivityCode((String) row.get("ActivityCode"));
					responseModel.setProfitCenter((String) row.get("pc_desc"));
					responseModel.setProfitCenterId((Integer) row.get("pc_id"));
					responseModel.setGlCode((String) row.get("GlCode"));
					responseModel.setActivitySourceName((String) row.get("ActivitySourceName"));
					responseModel.setActivityCostPerDay((Double) row.get("ActivityCostPerDay"));
					responseModel.setIsActive((Character) row.get("IsActive"));
					responseModel.setId((Integer) row.get("activity_id"));
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
	
	
	public Map<String, String> fetchActivityCodeIncrement(Session session) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityCodeIncrement invoked.." +  "::::::::::IncrementOrder::::::::::"
					);
		}
		Query query = null;
		Map<String, String> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [GET_ACTIVITY_CODE_GENERATOR] ";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				Integer activityPlanId = null;
				for (Object object : data) {
					Map row = (Map) object;
					String ActivityCode = (String) row.get("ActivityCode");
					responseListModel.put("ActivityCode", ActivityCode);
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
		return responseListModel;
	}

	@Override
	public List<ActivityGlCodeListModel> fetchActivityGLCodeList(String userCode, Object object1, int pcId) {
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"fetchActivityGLCodeList invoked.." + userCode + " " );
		}
		
		Session session = null;
		List<ActivityGlCodeListModel>  responseList = null;
		ActivityGlCodeListModel response = null;
	    NativeQuery<?> query = null;
		String sqlQuery = "exec [GET_GLCODE_LIST] :PcId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ActivityGlCodeListModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ActivityGlCodeListModel();
					response.setGlCode((String) row.get("gl_head_code"));
					response.setDisplayName((String) row.get("displayname"));
					responseList.add(response);
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
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
			int activitySourceMasterEntity = query1.executeUpdate();
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
	public List<ActivitySourceMasterResponseModel> fetchActivityNameList(String userCode, String searchFor,
			String activityName) {
		Session session = null;
		List<ActivitySourceMasterResponseModel> responseList = null;
		ActivitySourceMasterResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETACTIVITY_NAME_UNIQ_LIST] :searchFor, :ActivityName";
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
