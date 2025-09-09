/**
 * 
 */
package com.hitech.dms.web.dao.activityClaim.inv.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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

import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;
import com.hitech.dms.web.model.activityClaim.invoice.request.ActivityClaimInvRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.response.ActivityClaimInvDtlResponseModel;
import com.hitech.dms.web.model.activityClaim.invoice.response.ActivityClaimInvHdrResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityClaimInvDtlDaoImpl implements ActivityClaimInvDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvDtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ActivityCommonDao activityCommonDao;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActivityClaimInvHdrResponseModel fetchActivityClaimInvDTL(String userCode,
		ActivityClaimInvRequestModel requestModel) {
		Session session = null;
		Query query = null;
		Integer locationStateId=0;
		ActivityClaimInvHdrResponseModel responseModel = null;
		boolean compareResult=false;
		if(requestModel.getDealerId()!=null && requestModel.getLocation()!=null)
		{
			locationStateId=getStateIdByDistrict(userCode,requestModel.getLocation());
			System.out.println("after select location stateId "+locationStateId);
			//compareResult= compareSelectedLocation(userCode,requestModel.getDealerId(),requestModel.getLocation());
		}
		List<ActivityClaimInvDtlResponseModel> activityClaimInvDtlList = null;
		// while creating , send claim hdr ID, While Viewing, send claimInvoice hdr id
		String sqlQuery = "exec [SP_SACT_ACTIVITYCLAIM_INV_DESC] :userCode, :id, :isFor,:VST_State_Code";
		try {
			session = sessionFactory.openSession();

			ActivityClaimHdrResponseModel activityClaimHdrResponseModel = activityCommonDao
					.fetchActivityClaimHdr(session, userCode, requestModel.getId(), requestModel.getIsFor());
			if (activityClaimHdrResponseModel != null) {
				responseModel = mapper.map(activityClaimHdrResponseModel, ActivityClaimInvHdrResponseModel.class,
						"ActivityClaimHdrResMapId");
				BigInteger bi = BigInteger.valueOf(locationStateId.intValue());
				// get stateId using dealer and stateId based on selected location 
				System.out.println("before send query "+locationStateId);
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("id", requestModel.getId());
				query.setParameter("isFor", requestModel.getIsFor());
				query.setParameter("VST_State_Code",bi);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					ActivityClaimInvDtlResponseModel activityClaimInvDtlResponseModel = null;
					activityClaimInvDtlList = new ArrayList<ActivityClaimInvDtlResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						activityClaimInvDtlResponseModel = new ActivityClaimInvDtlResponseModel();
						activityClaimInvDtlResponseModel.setItemCode((String) row.get("item_code"));
						activityClaimInvDtlResponseModel.setItemDetails((String) row.get("item_dtl"));
						activityClaimInvDtlResponseModel.setApprovedAmount((BigDecimal) row.get("ApprovedAmount"));
						activityClaimInvDtlResponseModel.setCgstAmount((BigDecimal) row.get("cgst_amnt"));
						activityClaimInvDtlResponseModel.setCgstPercent((BigDecimal) row.get("cgst_per"));
						activityClaimInvDtlResponseModel.setDiscountAmount((BigDecimal) row.get("Discount_Amount"));
						activityClaimInvDtlResponseModel.setDiscountPercent((BigDecimal) row.get("Discount_Per"));
						activityClaimInvDtlResponseModel.setIgstAmount((BigDecimal) row.get("igst_amnt"));
						activityClaimInvDtlResponseModel.setIgstPercent((BigDecimal) row.get("igst_per"));
						activityClaimInvDtlResponseModel.setQuantity((Integer) row.get("qty"));
						activityClaimInvDtlResponseModel.setSgstAmount((BigDecimal) row.get("sgst_amnt"));
						activityClaimInvDtlResponseModel.setSgstPercent((BigDecimal) row.get("sgst_per"));
						activityClaimInvDtlResponseModel.setTotalAmount((BigDecimal) row.get("total_amnt"));
						activityClaimInvDtlResponseModel.setUnitPrice((BigDecimal) row.get("unit_price"));
						activityClaimInvDtlResponseModel.setNetAmnt((BigDecimal) row.get("net_amnt"));
						activityClaimInvDtlResponseModel.setGstAmount((BigDecimal) row.get("gst_amnt"));
						activityClaimInvDtlResponseModel.setGlCode((String)row.get("gl_code"));
						activityClaimInvDtlResponseModel.setHsnCode((String) row.get("hsn_code"));
						activityClaimInvDtlResponseModel.setActivityActualFromDate((Date)row.get("activity_actual_from_date"));
						activityClaimInvDtlResponseModel.setActivityActualToDate((Date)row.get("activity_actual_to_date"));
						

						activityClaimInvDtlList.add(activityClaimInvDtlResponseModel);
					}
					responseModel.setActivityClaimInvDtlList(activityClaimInvDtlList);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	/*
	 * private boolean compareSelectedLocation(String userCode, Integer dealerId,
	 * String location) { boolean stateIdStatus=false;
	 * logger.info("compareSelectedLocation => {}"+dealerId +" location "+location);
	 * Session session = null; BigInteger dealerStateId=BigInteger.ZERO; BigInteger
	 * locationStateId=BigInteger.ZERO; boolean isSuccess=false; Query query = null;
	 * try {
	 * 
	 * session = sessionFactory.openSession();
	 * 
	 * String sqlQuery =
	 * "select Dealer_State_ID from ADM_BP_DEALER where parent_dealer_id=:dealerId";
	 * query = session.createSQLQuery(sqlQuery); query.setParameter("dealerId",
	 * dealerId); query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP); List
	 * data = query.list(); if (data != null && !data.isEmpty()) { for (Object
	 * object : data) { Map row = (Map) object; dealerStateId=(BigInteger)
	 * row.get("Dealer_State_ID");
	 * System.out.println("stateId we get is "+dealerStateId); isSuccess=true;
	 * if(isSuccess) { locationStateId=getStateIdByDistrict(userCode,location); }
	 * if((dealerStateId!=null||dealerStateId.compareTo(BigInteger.ZERO) !=
	 * 0)&&(locationStateId!=null||dealerStateId.compareTo(BigInteger.ZERO) != 0)) {
	 * 
	 * if (dealerStateId.equals(locationStateId)) {
	 * System.out.println("BigIntegers are  equal");
	 * 
	 * stateIdStatus=true; } else { System.out.println("BigIntegers are not equal");
	 * 
	 * stateIdStatus=true;
	 * 
	 * } }
	 * 
	 * 
	 * } }
	 * 
	 * }catch (SQLGrammarException exp) { logger.error(this.getClass().getName(),
	 * exp); stateIdStatus=false; } catch (HibernateException exp) {
	 * logger.error(this.getClass().getName(), exp); stateIdStatus=false;
	 * 
	 * } catch (Exception exp) { logger.error(this.getClass().getName(), exp); }
	 * finally { if (session != null) { session.close(); stateIdStatus=false;
	 * 
	 * 
	 * } }
	 * 
	 * return stateIdStatus; }
	 */
	
		private Integer getStateIdByDistrict(String userCode,String location) {
		Integer stateId=0;
		logger.info("compareSelectedLocation => {} location "+location);
		Session session = null;
		boolean isSuccess=false;
		Query query = null;
		try
		{
			
			session = sessionFactory.openSession();
			
			//String sqlQuery = "select state_id from CM_GEO_DIST where DistrictDesc=:location";
			String sqlQuery = "select statecode as state_id from SA_MST_PO_PLANT where Plant_code=:location";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("location", location);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					 stateId=(Integer) row.get("state_id");
				    System.out.println("stateId for location get is "+stateId);
				    isSuccess=true;
				    
				    
				}
			}
			
		}catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			isSuccess=false;
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			isSuccess=false;

		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
				isSuccess=false;

				
			}
		}
		
		return stateId;
	}
}
