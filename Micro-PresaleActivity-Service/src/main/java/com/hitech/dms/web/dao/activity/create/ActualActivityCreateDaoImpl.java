/**
 * 
 */
package com.hitech.dms.web.dao.activity.create;

import java.math.BigInteger;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.activity.ActualActivityENQEntity;
import com.hitech.dms.web.entity.activity.ActualActivityENQPEntity;
import com.hitech.dms.web.entity.activity.ActualActivityHDREntity;
import com.hitech.dms.web.model.activity.create.request.ActualActivityForENQRequestModel;
import com.hitech.dms.web.model.activity.create.request.ActualActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActualActivityResponeModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActualActivityCreateDaoImpl implements ActualActivityCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(ActualActivityCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	public ActualActivityResponeModel createActualActivity(String userCode, ActualActivityRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityNameListBasedOnActivityPlanId invoked..");
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		List<ActualActivityForENQRequestModel> enquiryList = requestModel.getEnquiryList();
		ActualActivityResponeModel responseModel = new ActualActivityResponeModel();
		BigInteger activityActualHdrId = null;
		boolean isSuccess = true;
		String msg = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				ActualActivityHDREntity activityHDREntity = mapper.map(requestModel, ActualActivityHDREntity.class,
						"ActualActivityCreateMapId");
				if (activityHDREntity.getActivityActualDate() == null) {
					activityHDREntity.setActivityActualDate(new Date());
				}
				// current date
				Date currDate = new Date();
				activityHDREntity.setActivityActualNo(
						"ACT/" + activityHDREntity.getActivityId() + "/" + activityHDREntity.getActivityPlanHdrId()
								+ "/" + activityHDREntity.getDealerId() + "/" + currDate.getTime());
				activityHDREntity.setCreatedBy(userId);
				activityHDREntity.setCreatedDate(new Date());

				session.save(activityHDREntity);

				for (ActualActivityForENQRequestModel enqRequestModel : enquiryList) {
					ActualActivityENQEntity activityENQEntity = new ActualActivityENQEntity();
					ActualActivityENQPEntity activityENQPEntity = new ActualActivityENQPEntity();

					activityENQPEntity.setActivityActualHdrId(activityHDREntity.getActivityActualHdrId());
					activityENQPEntity.setEnquiryId(enqRequestModel.getEnquiryId());

					activityENQEntity.setActualActivityPENQ(activityENQPEntity);

					session.save(activityENQEntity);
				}

				activityActualHdrId = activityHDREntity.getActivityActualHdrId();
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setActivityActualHdrId(activityActualHdrId);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Actual Activity Created Successfully";
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
			if (isSuccess) {
				mapData = fetchActualActivityNoByActualActivityId(activityActualHdrId);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setActivityActualNo((String) mapData.get("actualActivityNo"));
				}
			}else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActualActivityNoByActualActivityId(BigInteger activityActualHdrId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select activity_actual_number from SA_ACT_ACTUAL_HDR (nolock) saah where saah.activity_actual_hdr_id =:activityActualHdrId";
		mapData.put("ERROR", "Actual Activity Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityActualHdrId", activityActualHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String actualActivityNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					actualActivityNo = (String) row.get("activity_actual_number");
				}
				mapData.put("actualActivityNo", actualActivityNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTUAL ACTIVITY DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTUAL ACTIVITY DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
}
