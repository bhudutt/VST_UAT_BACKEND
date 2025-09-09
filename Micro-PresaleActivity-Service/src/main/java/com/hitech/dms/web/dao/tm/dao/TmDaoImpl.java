package com.hitech.dms.web.dao.tm.dao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.tm.TmEnquiryTransferEntity;
import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferRequestModel;
import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferResponseModel;
import com.hitech.dms.web.model.tm.create.response.TmListFormModel;
import com.hitech.dms.web.model.tm.create.response.TmListModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQRequestModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQResponse;



@Repository
public class TmDaoImpl implements TmDao {
	
	private static final Logger logger = LoggerFactory.getLogger(TmDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<TmListModel> fetchTMList(String userCode, Long dealerOrBranchID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchtmList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<TmListModel> tmListModelList = null;
		String sqlQuery = "exec [SP_GET_Territory_Manager_BranchId] :usercode, :territory_id, :branchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setParameter("territory_id", 0);
			query.setParameter("branchId", dealerOrBranchID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				tmListModelList = new ArrayList<TmListModel>();
				TmListModel tmModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					tmModel = new TmListModel();
					tmModel.setTmID((BigInteger) row.get("tmId"));
					tmModel.setTmName((String) row.get("tmName"));
					tmListModelList.add(tmModel);
				}
			}

		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return tmListModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<TmTransferENQResponse> fetchTmTransferENQList(String userCode, TmTransferENQRequestModel enqRequestModel) {
//		if (logger.isDebugEnabled()) {
			logger.debug("fetchTransferENQList invoked.." + enqRequestModel.toString());
//		}
		Session session = null;
		Query query = null;
		List<TmTransferENQResponse> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [SP_ENQ_getTm_TransferENQdtls]  :branchID, :userCode, :tmPersonID, :enquiryNo,"
					+ ":enquiryFromDate, :enquiryToDate, :includeInactive";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchID", enqRequestModel.getBranchID());
			query.setParameter("userCode", userCode);
			query.setParameter("tmPersonID", enqRequestModel.getTmPersonID());
			query.setParameter("enquiryNo", enqRequestModel.getEnquiryNo());
			query.setParameter("enquiryFromDate", enqRequestModel.getEnquiryFromDate());
			query.setParameter("enquiryToDate", enqRequestModel.getEnquiryToDate());
			query.setParameter("includeInactive", enqRequestModel.getIncludeInactive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<TmTransferENQResponse>();
				TmTransferENQResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new TmTransferENQResponse();
					
					  responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					  responseModel.setEnquiryNumber((String) row.get("enquiryNumber"));
					  responseModel.setEnquiryDate((String) row.get("enquiry_date"));
					  responseModel.setEnquiryStage((String) row.get("enq_stage"));
					  responseModel.setEnquiryStatus((String) row.get("enquiry_status"));
					  responseModel.setEnquiryType((String) row.get("enquiry_type"));
					  responseModel.setSalesman((String) row.get("dsp_name"));
					  responseModel.setModelPlusVariant((String) row.get("modelPlusVariant"));
					  responseModel.setVillage((String) row.get("Village"));
					  responseModel.setDistrict((String) row.get("district"));
					  responseModel.setCity((String) row.get("City"));
					  responseModel.setTehsil((String) row.get("tehsil"));
					  responseModel.setCustomerName((String) row.get("customer_name"));
					  responseModel.setMobileNumber((String) row.get("mobile_no"));
					  responseModel.setState((String) row.get("state"));
					  responseModel.setCountry((String) row.get("country"));
					 
					responseModelList.add(responseModel);
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
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public EnquiryTmTransferResponseModel transferEnqTm(String authorizationHeader, String userCode,
			EnquiryTmTransferRequestModel enquiryTransferRequestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		TmEnquiryTransferEntity enquiryTransferEntity = null;
		EnquiryTmTransferResponseModel responseModel = new EnquiryTmTransferResponseModel();
		boolean isSuccess = false;
		//String sqlQuery = "update SA_ENQ_HDR set salesman_id =:salesmanId, ModifiedBy =:modifiedBy, ModifiedDate = GetDate() where enquiry_id in (:enquiryIdList) and branch_id =:branchId and salesman_id =:salesmanFromId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				// validate TransferToId
//				mapData = validateSalesman(authorizationHeader, enquiryTransferRequestModel.getTransferToId(),
//						enquiryTransferRequestModel.getBranchId());
				//if (mapData != null && mapData.get("SUCCESS") != null)
				if (true)
				{
					//query = session.createNativeQuery(sqlQuery);
					//query.setParameterList("enquiryIdList", enquiryTransferRequestModel.getEnquiryIdList());
					//query.setParameter("branchId", enquiryTransferRequestModel.getBranchId());
					//query.setParameter("salesmanId", enquiryTransferRequestModel.getTransferToId());
					//query.setParameter("salesmanFromId", enquiryTransferRequestModel.getTransferFromId());
					//query.setParameter("modifiedBy", userId);
					//int result = query.executeUpdate();
					//if (result > 0) {
						if (enquiryTransferRequestModel.getEnquiryIdList() != null
								&& !enquiryTransferRequestModel.getEnquiryIdList().isEmpty()) {
							for (BigInteger enquiryId : enquiryTransferRequestModel.getEnquiryIdList()) {
								enquiryTransferEntity = new TmEnquiryTransferEntity();
								enquiryTransferEntity.setEnquiryHdrId(enquiryId);
								enquiryTransferEntity
										.setTransferFromId(enquiryTransferRequestModel.getTransferFromId());
								enquiryTransferEntity.setTransferToId(enquiryTransferRequestModel.getTransferToId());
								enquiryTransferEntity.setTransferDate(new Date());
								enquiryTransferEntity.setCreatedBy(userId);
								enquiryTransferEntity.setCreatedDate(new Date());
								session.save(enquiryTransferEntity);
							}
						//}
						responseModel.setMsg("TM Transfered Successfully.");
						isSuccess = true;
						transaction.commit();
					} else {
						// enquiry ids not found
						responseModel.setMsg("Selected Enquiries not found.");
					}
				} else {
					// Transfer Salesman not found
					responseModel.setMsg("Selected Transfer TM not found.");
				}
			} else {
				// user not found
				responseModel.setMsg("User not found");
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg("Software Exception, please inform.");
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg("Software Exception, please inform.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg("Software Exception, please inform.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if(isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
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
	
	/*
	 * @SuppressWarnings("unchecked") private Map<String, Object>
	 * validateSalesman(String authorizationHeader, BigInteger salesmanTransferId,
	 * BigInteger branchId) { Map<String, Object> mapData = new HashMap<String,
	 * Object>(); TmListFormModel formModel = new TmListFormModel();
	 * formModel.setDealerOrBranch("BRANCH");
	 * formModel.setDealerOrBranchID(branchId.longValue()); HeaderResponse
	 * userAuthResponse = salesmanServiceClient.getSalesmanList(authorizationHeader,
	 * "BRANCH", branchId.longValue()); Object object =
	 * userAuthResponse.getResponseData(); List<SalesmanListModel> salesmanList =
	 * null; if(object != null) { try { String jsonString = new
	 * com.google.gson.Gson().toJson(object); salesmanList =
	 * jsonArrayToObjectList(jsonString, SalesmanListModel.class);
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * logger.error(this.getClass().getName(), e); } } if (salesmanList != null &&
	 * !salesmanList.isEmpty()) { boolean isExist = salesmanList.stream()
	 * .anyMatch(salesmanModel ->
	 * salesmanTransferId.compareTo(salesmanModel.getSalesmanID()) == 0);
	 * if(isExist) { mapData.put("SUCCESS", true); }else { mapData.put("SUCCESS",
	 * "Salesman Not Found."); } } else { mapData.put("ERROR",
	 * "Salesman Not Found."); }
	 * 
	 * return mapData; }
	 */

}
