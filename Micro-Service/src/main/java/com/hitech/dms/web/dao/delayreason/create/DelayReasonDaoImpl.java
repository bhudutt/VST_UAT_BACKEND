package com.hitech.dms.web.dao.delayreason.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.master.delayreason.request.DelayReasonMasterEntity;
import com.hitech.dms.web.entity.master.storemaster.request.StoreMasterEntity;
import com.hitech.dms.web.model.delayreason.create.request.DelayReasonRequestName;
import com.hitech.dms.web.model.delayreason.create.response.DelayReasonResponseModel;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;

@Repository
public class DelayReasonDaoImpl implements DelayReasonDao{

private static final Logger logger = LoggerFactory.getLogger(DelayReasonDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private CommonDao commonDao;
	
	
	@Override
	public DelayReasonResponseModel createDelayReasonMaster(String authorizationHeader, String userCode,
			DelayReasonRequestName requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createStore invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DelayReasonResponseModel responseModel = new DelayReasonResponseModel();
		DelayReasonMasterEntity entity=null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Date todayDate = new Date();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			if (null != requestModel ) {
					entity = mapper.map(requestModel, DelayReasonMasterEntity.class, "delayReasonMasterMapId");
					if (entity.getDelayReasonId() != null) {
						entity.setModifiedBy(userCode);
						entity.setModifiedDate(todayDate);
						session.update(entity);
						transaction.commit();
					} else {
						entity.setCreatedBy(userCode);
						entity.setCreatedDate(todayDate);
						Integer id = (Integer) session.save(entity);
						if(id>0) {
							transaction.commit();
						}else {
							isSuccess=false;
						}
					}
			}
			
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}finally {
			if (isSuccess) {
				responseModel.setMsg("Delay Reason Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				responseModel.setMsg("Some Issue While Creating Delay Reason.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		

		return responseModel;
	}
	
	@Override
	public List<DelayReasonResponseModel> searchDelayReason(String userCode){
		if (logger.isDebugEnabled()) {
			logger.debug("searchDelayReason invoked..");
		}
		Query query = null;
		List<DelayReasonResponseModel> responseModelList = null;
		DelayReasonResponseModel delayReasonResponseModel=null;
		Session session = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from SV_REASON_TYPE ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseModelList = new ArrayList<DelayReasonResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					delayReasonResponseModel = new DelayReasonResponseModel();
					delayReasonResponseModel.setReasonId((Integer) row.get("delay_reason_id"));
					delayReasonResponseModel.setReasonCode((String) row.get("delay_reason_code"));
					delayReasonResponseModel.setReasonDesc((String) row.get("delay_reason_desc"));
					Character activeStatus=(Character) row.get("IsActive");
					if(activeStatus.equals('Y')) {
						delayReasonResponseModel.setStatus(true);
					}else {
						delayReasonResponseModel.setStatus(false);
					}
					
					responseModelList.add(delayReasonResponseModel);
				}

				//delayReasonResponseModel.setRecordCount(recordCount);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
			session.flush();
		}
		return responseModelList;
	
	}

}
