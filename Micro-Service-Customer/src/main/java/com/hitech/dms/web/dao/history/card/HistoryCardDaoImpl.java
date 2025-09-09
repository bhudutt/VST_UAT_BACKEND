package com.hitech.dms.web.dao.history.card;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
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

import com.hitech.dms.web.model.service.report.request.HistoryCardDtlResponse;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponse;

@Repository
public class HistoryCardDaoImpl implements HistoryCardDao {
	
	private static final Logger logger = LoggerFactory.getLogger(HistoryCardDaoImpl.class);
	
	
	@Autowired
	private SessionFactory  sessionFactory;

//	@Override
//	public List<HistoryCardResponse> getHistoryCardDetailList(String userCode, String chassis) {
//
//
//		Session session = null;
//		Query query = null;
//		HistoryCardResponse responseModel=null;
//		List<HistoryCardResponse>  responseListModel= null;
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		
//		String sqlQuery = "exec service_history_card :chassis,:flag";
//		
//		try {
//			session = sessionFactory.openSession();
//			query = session.createSQLQuery(sqlQuery);
//			query.setParameter("chassis", chassis);
//			query.setParameter("flag", 1);
////			query.setParameter("page", bean.getPage());
////			query.setParameter("size", bean.getSize());
//			
//			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//			List data = query.list();
//			if (data != null && !data.isEmpty()) {
//				
//				responseListModel = new ArrayList<>();
//				for (Object object : data) {
//					Map row = (Map) object;
//					
//					responseModel = new HistoryCardResponse();
//	
////					responseModel.setEngineNo((String) row.get("EngineNo"));
////					responseModel.setChassisNo((String) row.get("ChassisNo"));		
////				    responseModel.setItemNo((String) row.get("ItemNumber"));
////				    responseModel.setItemDescription((String) row.get("ItemDescription"));
////				    responseModel.setModelName((String) row.get("Model"));
////				    responseModel.setParentDealerCode((Integer) row.get("DealerCode"));
////				    responseModel.setParentDealerName((String) row.get("DealerName"));
////				    responseModel.setDealerState((String) row.get("DealerState"));
////				    responseModel.setCustomerName((String) row.get("CustomerName"));
////				    responseModel.setAddress1((String) row.get("CustomerAddress"));
////				    responseModel.setMobileNo((String) row.get("MobileNo"));
////				    responseModel.setPinCode((String) row.get("PinCode"));
////				    responseModel.setCityDesc((String) row.get("City"));
////				    responseModel.setTehsilDesc((String) row.get("Tehsil"));
////				    responseModel.setDistrictDesc((String) row.get("CustomerDistrict"));
////				    responseModel.setStateDesc((String) row.get("DealerState"));
////				    responseModel.setDeliveryChallanDate((Date) row.get("DeliveryChallanDate"));
////				    responseModel.setPreviousServiceName((String)row.get("PreviousServiceName"));
////				    responseModel.setPreviousServiceDate((Date)row.get("PreviousServiceDate"));
////				    responseModel.setPreviousServiceHours((Integer)row.get("PreviousServiceHours"));
////				    responseModel.setNextServiceName((String)row.get("NextServiceName"));
////				    responseModel.setNextServiceDate((Date)row.get("NextServiceDate"));
////				    responseModel.setNoOfDaysFromDateOfDelivery((Integer)row.get("NoOfDaysFromDateOfDelivery"));
//				    
//					responseListModel.add(responseModel);
//				}
//			}
//		} catch (SQLGrammarException exp) {
//			logger.error(this.getClass().getName(), exp);
//		} catch (HibernateException exp) {
//			logger.error(this.getClass().getName(), exp);
//		} catch (Exception exp) {
//			logger.error(this.getClass().getName(), exp);
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//		return responseListModel;
//	}

	@Override
	public HistoryCardResponse getHistoryCardHdrDetail(String userCode, String chassisNo) {
	
		Session session = null;
		Query query = null;
		HistoryCardResponse responseModel=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String sqlQuery = "exec service_history_card :chassis,:flag";
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("chassis", chassisNo);
			query.setParameter("flag", 1);
//			query.setParameter("page", bean.getPage());
//			query.setParameter("size", bean.getSize());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
				
						
					responseModel = new HistoryCardResponse();
	
					responseModel.setCustomerAddress((String) row.get("customerAddress"));
					
					responseModel.setCustomerName((String) row.get("customername"));
					responseModel.setDealerName((String) row.get("dealerName"));	
					responseModel.setDealerCode((String) row.get("dealerCode"));
				    responseModel.setDealerLocation((String) row.get("branchLocation"));
				    responseModel.setVinNo((String) row.get("VinNumber"));
				    responseModel.setModelDesc((String) row.get("ModelDesc"));
				    responseModel.setEngineNumber((String) row.get("EngineNo"));
				    responseModel.setDateOfVstInvoice((String) row.get("dateofvstInvoice"));
				    responseModel.setChassisNumber((String) row.get("ChassisNumber"));
				    responseModel.setCustomerMobileNo1((String) row.get("MobileNumber"));
				    responseModel.setCustomerMobileNo2((String) row.get("alternateMobNo"));
				    responseModel.setDateOfDelivery((String) row.get("SaleDate"));
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
		return responseModel;
	
	}

	@Override
	public List<HistoryCardDtlResponse> getHistoryCardDetailList(String userCode, String chassisNo) {
	
		Session session = null;
		Query query = null;
		HistoryCardDtlResponse responseModel=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<HistoryCardDtlResponse> cardList = new ArrayList<>();
		
		
		try {
			String sqlQuery = "exec service_history_card :chassis,:flag";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("chassis", chassisNo);
			query.setParameter("flag", 2);
//			query.setParameter("page", bean.getPage());
//			query.setParameter("size", bean.getSize());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				
				for (Object object : data) {
					Map row = (Map) object;
				
						
					responseModel = new HistoryCardDtlResponse();
	
					responseModel.setDate((String) row.get("Date"));
					
					responseModel.setRoId((BigInteger) row.get("ro_id"));
					responseModel.setRONumber((String) row.get("RONumber"));	
					responseModel.setTotal_Hour((BigInteger) row.get("Total_Hour"));
				    responseModel.setJobCardcategory((String) row.get("jobCardcategory"));
				    responseModel.setServiceType((String) row.get("serviceType"));
				    responseModel.setSrvTypeDesc((String) row.get("SrvTypeDesc"));
				    responseModel.setObservation((String) row.get("observation"));
				    responseModel.setActivityDone((String) row.get("ActivityDone"));
				    responseModel.setActiontaken((String) row.get("Actiontaken"));
				    responseModel.setPartreplaced((String) row.get("partreplaced"));
				    responseModel.setStatusofjob((String) row.get("statusofjob"));
				    cardList.add(responseModel);
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
		return cardList;
	}

}
