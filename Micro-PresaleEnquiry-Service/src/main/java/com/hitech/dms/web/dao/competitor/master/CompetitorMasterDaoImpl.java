package com.hitech.dms.web.dao.competitor.master;


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.competitor.master.CompetitorMasterEntity;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterBrandListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterBrandRequestModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterResponseModel;


@Repository
public class CompetitorMasterDaoImpl implements CompetitorMasterDao{
	private static final Logger logger = LoggerFactory.getLogger(CompetitorMasterDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Override
	public CompetitorMasterResponseModel createCompetitorMaster(String userCode,
			CompetitorMasterEntity competitorMasterEntity, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("competitorMasterEntity invoked.." + userCode);
			logger.debug(competitorMasterEntity.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		CompetitorMasterResponseModel responseModel = new CompetitorMasterResponseModel();
		Query query = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		    session.save(competitorMasterEntity);
		
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Competitor Master Created Successfully";
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
	public List<CompetitorMasterListResponseModel> fetchCompetitorMasterList(String userCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceMasterList invoked.." + userCode);
			

		}
		Session session = null;
		
		List<CompetitorMasterListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			// pc_id, brand_id
			responseModelList = fetchCompetitorMasterList(session, userCode);
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
	public List<CompetitorMasterListResponseModel> fetchCompetitorMasterList(Session session, String userCode
			) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceMasterList invoked.." + userCode + " ");
			
		}
		Query query = null;
		List<CompetitorMasterListResponseModel> responseModelList = null;
		String sqlQuery = "Select CMT.ID, PRO.pc_desc,BRD.BRAND_NAME, cmt.IsActive from COMPETITOR_MASTER CMT join ADM_BP_MST_PROFIT_CENTER PRO on PRO.pc_id=CMT.pc_id join CM_MST_BRAND BRD on CMT.brand_id=BRD.brand_id";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CompetitorMasterListResponseModel>();
				CompetitorMasterListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CompetitorMasterListResponseModel();
					responseModel.setId((Integer)row.get("ID"));
					responseModel.setProfitCenter((String) row.get("pc_desc"));
					responseModel.setBrandName((String) row.get("BRAND_NAME"));
					responseModel.setActive((Character) row.get("IsActive"));
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
	
	//New

	
	//End

	
	public List<CompetitorMasterBrandListResponseModel> fetchfetchBrandList(String userCode,
			CompetitorMasterBrandRequestModel competitorMasterBrandRequestModel) {
		
		Session session = null;
		List<CompetitorMasterBrandListResponseModel> responseModelList = null;
		CompetitorMasterBrandListResponseModel responseModel = null;
		Query<CompetitorMasterBrandListResponseModel> query = null;
		String sqlQuery = "Select brand_id, BRAND_NAME from CM_MST_BRAND";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CompetitorMasterBrandListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new CompetitorMasterBrandListResponseModel();
					
					responseModel.setBrandid((BigInteger) row.get("brand_id"));
					responseModel.setBrandName((String) row.get("BRAND_NAME"));
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
	public CompetitorMasterResponseModel changeActiveStatus(String userCode,Integer id,Character isActive) 
	   {
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		CompetitorMasterResponseModel responseModel = new CompetitorMasterResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query1 = session.createQuery("UPDATE CompetitorMasterEntity SET IsActive = :isActive WHERE ID = :id");
			query1.setParameter("isActive", isActive);
			query1.setParameter("id", id);
			int competitorMasterEntity1 = query1.executeUpdate();
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			
			msg = "Active Status Update Successfully";
			responseModel.setMsg(msg);
		}else {
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			msg = "Active Status Not Update Successfully";
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




}
