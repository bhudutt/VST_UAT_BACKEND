package com.hitech.dms.web.dao.baymaster.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.hitech.dms.web.dao.service.booking.ServiceBookingDaoImpl;
import com.hitech.dms.web.entity.baymaster.BayMasterEntity;
import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.baymaster.responselist.BayMasterResponseModel;
import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;
import com.hitech.dms.web.model.baymaster.create.request.BayMasterRequest;
import com.hitech.dms.web.model.oldchassis.OldChassisCreateResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListResponseModel;

@Repository
public class BayMasterDaoImpl implements BayMasterDao {

	private static final Logger logger = LoggerFactory.getLogger(BayMasterDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public BayMasterResponse createBayMaster(String userCode, List<BayMasterRequest> bayMasterRequestList) {
		if (logger.isDebugEnabled()) {
			logger.debug("createBayMaster invoked.." + userCode);
			logger.debug(bayMasterRequestList.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		BayMasterResponse bayMasterResponse = new BayMasterResponse();
		List<BayMasterResponse> bayMasterResponseList = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				for (BayMasterRequest bayMasterRequest : bayMasterRequestList) {
					BayMasterEntity bayMasterEntity = new BayMasterEntity();
					String sqlQuery = "exec [sp_insert_bay_master] :baycode, :baydesc, :baytype,"
							+ ":dealerid, :IsActive, :createdby";
					
					query = session.createSQLQuery(sqlQuery);

					query.setParameter("baycode", bayMasterRequest.getBayCode());
					query.setParameter("baydesc", bayMasterRequest.getBayDesc());
					query.setParameter("baytype", bayMasterRequest.getBayType());
					query.setParameter("dealerid", bayMasterRequest.getDealerId());
					if (bayMasterRequest.isActive()) {
						active = "Y";
					}
					query.setParameter("IsActive", active);
					query.setParameter("createdby", userId);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

					List<?> data = query.list();

					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map row = (Map) object;
							msg = (String) row.get("Message");
							if (msg.equalsIgnoreCase("Data Already Exists")) {
								isSuccess = false;
								statusCode = WebConstants.STATUS_INTERNAL_SERVER_ERROR_500;
							}
							bayMasterResponse.setMsg(msg);
//							return bayMasterResponse;
						}
					}
				}	
			}
			
			if (isSuccess) {
				transaction.commit();
				session.close();
//				bayMasterResponse.setBayCode(.getBayCode());
				bayMasterResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Bay Master Created Successfully";
				bayMasterResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				bayMasterResponse.setStatusCode(statusCode);
				bayMasterResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bayMasterResponse;
	}

	@Override
	public BayMasterResponse updateBayMaster(String userCode, Integer bayMasterId, String isActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateBayMaster invoked.." + userCode);
			logger.debug(bayMasterId.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		Map<String, Object> mapData = null;
		BayMasterResponse bayMasterResponse = new BayMasterResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger userId = null;
		try {
			String sqlQuery = "update BAY_MASTER set is_active =:IsActive,  modified_date =:ModifiedDate ,"
					+ " modified_by =:ModifiedBy where bay_master_id = :bayMasterId";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("bayMasterId", bayMasterId);
				query.setParameter("IsActive", isActive);
				query.setParameter("ModifiedDate", todayDate);
				query.setParameter("ModifiedBy", userId);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				int queryUpdate = query.executeUpdate();
				if(queryUpdate == 0) {
					isSuccess = false;
				}

			}
			
		if (isSuccess) {
			transaction.commit();
			session.close();
//			bayMasterResponse.setBayCode(.getBayCode());
			bayMasterResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Bay Master Updated Successfully";
			bayMasterResponse.setMsg(msg);
		} else {
			transaction.commit();
			session.close();
			bayMasterResponse.setStatusCode(statusCode);
			bayMasterResponse.setMsg(msg);
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			bayMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return bayMasterResponse;
	}

	@Override
	public List<BayMasterResponseModel> fetchBayMasterList(String userCode, Integer dealerId, 
			Integer page, Integer size) {

		Session session = null;
		List<BayMasterResponseModel> bayMasterResponseModelList = null;
		BayMasterResponseModel bayMasterResponseModel = null;
		Query<BayMasterResponseModel> query = null;
		String sqlQuery = "exec [Get_Bay_Master_Data] :DealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DealerId", dealerId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				bayMasterResponseModelList = new ArrayList<BayMasterResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					bayMasterResponseModel = new BayMasterResponseModel();
					bayMasterResponseModel.setId((Integer) row.get("bay_master_id"));
					bayMasterResponseModel.setBayType((String) row.get("bay_type"));
					bayMasterResponseModel.setBayCode((String) row.get("bay_code"));
					bayMasterResponseModel.setBayDesc((String) row.get("bay_desc"));
					Character isActive = (Character) row.get("is_active");
					if (isActive.equals('Y')) {
						bayMasterResponseModel.setActive("Y");
					} else {
						bayMasterResponseModel.setActive("N");
					}
					bayMasterResponseModelList.add(bayMasterResponseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return bayMasterResponseModelList;
	}

	@Override
	public List<BayTypeModel> fetchBayTypeList(String bayType) {

		Session session = null;
		List<BayTypeModel> bayTypeModelList = null;
		BayTypeModel bayTypeModel = null;
		Query<BayMasterResponseModel> query = null;
		String sqlQuery = "select lookup_id, LookupVal, DisplayOrder from SYS_LOOKUP where LookupTypeCode = 'BAY_TYPE'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				bayTypeModelList = new ArrayList<BayTypeModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					bayTypeModel = new BayTypeModel();
					bayTypeModel.setValueId((BigInteger) row.get("lookup_id"));
					bayTypeModel.setValueCode((String) row.get("LookupVal"));
					bayTypeModel.setDisplayValue((Integer) row.get("DisplayOrder"));

					bayTypeModelList.add(bayTypeModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return bayTypeModelList;
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
