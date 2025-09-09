package com.hitech.dms.web.dao.enquiry.productTrial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.productTrial.ProductTrialHdrEntity;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialAttributeResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialCreateResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnquiryHistoryResponse;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class ProductTrialCreateDaoImpl implements ProductTrialCreateDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductTrialCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	Integer size = Integer.MAX_VALUE-1;
	
	@Override
	public ProductTrialEnqProspectHistoryResponse fetchEnqProspectEnqHistory(String userCode,PaymentReceiptViewRequestModel requestModel) {
		ProductTrialEnqProspectHistoryResponse responseModelList = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			responseModelList = new ProductTrialEnqProspectHistoryResponse();

			ProductTrialEnqProspectResponseModel enqProRes = fetchEnqProspectDtl(session, userCode, requestModel.getId(), 1);
	
			if (enqProRes !=null) {
				responseModelList.setEnqProspect(enqProRes);
				List<ProductTrialEnquiryHistoryResponse>  engHistory = fetchEnqHistory(session, requestModel.getId(),0,size,"create");
				List<ProductTrialAttributeResponse> feedback = fetchTrialAttribute(session, requestModel.getId(),"create");
				if (engHistory !=null) {
					responseModelList.setEnquiryHistory(engHistory);
				}
				if (feedback!=null) {
					responseModelList.setAttribute(feedback);
				}
			}

			
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}		
		return responseModelList;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	public ProductTrialEnqProspectResponseModel fetchEnqProspectDtl(Session session, String userCode, BigInteger enquiryId, int flag) {
		NativeQuery<?> query = null;
		ProductTrialEnqProspectResponseModel enqPros = null;
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					enqPros = new ProductTrialEnqProspectResponseModel();
					enqPros.setEnquiryNumber((String) row.get("enquiry_number"));
					enqPros.setEnquiryId((BigInteger) row.get("enquiry_id"));
					enqPros.setEnquiryStatus((String) row.get("enquiry_status"));
					enqPros.setEnquiryDate((String) row.get("enquiry_date"));
					enqPros.setSourceOfEnquiry((String) row.get("SourceOfEnquiry"));
					enqPros.setEnquiryStage((String) row.get("EnquiryStage"));
					enqPros.setModelId((BigInteger) row.get("model_id"));;
					enqPros.setModelName((String) row.get("model_name"));
					enqPros.setModelDesc((String) row.get("model_desc"));
					enqPros.setSeriseName((String) row.get("series_name"));
					enqPros.setSegmentName((String) row.get("segment_name"));    
					enqPros.setVariant((String) row.get("variant"));
					enqPros.setPcId((Integer) row.get("pc_id"));
					enqPros.setProfitCenter((String) row.get("Profit_Center"));
					enqPros.setMobileNo((String) row.get("Mobile_No"));
					enqPros.setProspectCategory((String) row.get("ProspectCategory"));
					enqPros.setProspectCode((String) row.get("ProspectCode"));
					enqPros.setTitle((String) row.get("Title"));
					enqPros.setCompanyName((String) row.get("CompanyName"));
					enqPros.setFirstName((String) row.get("FirstName"));
					enqPros.setMiddleName((String) row.get("MiddleName"));
					enqPros.setLastName((String) row.get("LastName"));
					enqPros.setWhatsappNo((String) row.get("WhatsappNo"));
					enqPros.setAlternateNo((String) row.get("Alternate_No"));
					enqPros.setPhoneNumber((String) row.get("PhoneNumber"));
					enqPros.setEmailid((String) row.get("Email_id"));
					enqPros.setAddress1((String) row.get("address1"));
					enqPros.setAddress2((String) row.get("address2"));
					enqPros.setAddress3((String) row.get("address3"));
					enqPros.setPincode((String) row.get("pincode"));
					enqPros.setVillage((String) row.get("village"));
					enqPros.setTehsil((String) row.get("tehsil"));
					enqPros.setDistrict((String) row.get("district"));
					enqPros.setState((String) row.get("state"));
					enqPros.setCountry((String) row.get("country"));
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return enqPros;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<ProductTrialEnquiryHistoryResponse> fetchEnqHistory(Session session, BigInteger id, int page,  int size, String flag) {
		NativeQuery<?> query = null;
		
		ProductTrialEnquiryHistoryResponse history;
		List<ProductTrialEnquiryHistoryResponse> historyList = null;
		
		String sqlQuery = "exec [sp_getProduct_Trial_History] :id,:page, :size, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("id", id);
			query.setParameter("page", page);
			query.setParameter("size", size);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				historyList = new ArrayList<ProductTrialEnquiryHistoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
						history = new ProductTrialEnquiryHistoryResponse();
						history.setEnquiryNumber((String) row.get("enquiry_number"));
						history.setProductTrailNo((String) row.get("Product_trial_No"));
						history.setProductTrailDate((String) row.get("Product_trial_Date"));
						history.setModelName((String) row.get("model_name"));
						history.setChassisNo((String) row.get("Chassis_no"));
						history.setOverallRating((BigDecimal) row.get("Overall_Rating"));
						history.setRemarks((String) row.get("Remarks"));
						history.setTrailGivenBy((String) row.get("Trial_Given_By"));
						historyList.add(history);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} 
		return historyList;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public 	List<ProductTrialAttributeResponse> fetchTrialAttribute(Session session, BigInteger id, String flag) {
		NativeQuery<?> query = null;
		ProductTrialAttributeResponse attribute= null;
		List<ProductTrialAttributeResponse> attributeList = null;
		String sqlQuery = "exec [SP_Enq_getTrial_Attribute] :id,:flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("id", id);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				attributeList = new ArrayList<ProductTrialAttributeResponse>();
				for (Object object : data) {
					Map row = (Map) object;
						attribute = new ProductTrialAttributeResponse();
						attribute.setTrialAttributeId((BigInteger) row.get("Trial_Attribute_Id"));
						attribute.setAttributeCode((String) row.get("Attribute_Code"));
						attribute.setAttributeDesc((String) row.get("Attribute_Desc"));
						if (flag.equals("view")) {
							attribute.setRating((int) row.get("Rating"));
							attribute.setFeedbackRemarks((String) row.get("Remarks"));
						}

						attributeList.add(attribute);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return attributeList;
	}


	@Override
	public ProductTrialCreateResponseModel createProductTrial(String userCode, ProductTrialHdrEntity ptEntity, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("Product Trial  invoked.." + userCode);
		}
		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;

		Date currDate = new Date();
		BigInteger productTrialId = null ;
		
		ProductTrialCreateResponseModel response = new ProductTrialCreateResponseModel();
		try {
			session = sessionFactory.openSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			transaction = session.beginTransaction();
			ptEntity.setProductTrialNo("PTN");
			ptEntity.setCreatedBy(userId);
			ptEntity.setCreatedDate(currDate);
			productTrialId = (BigInteger) session.save(ptEntity);
			transaction.commit();
			if (productTrialId != null) {
				response.setEnquiryHistory(fetchEnqHistory(session, ptEntity.getEnquiryId(),0,size,"create"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction.isActive()) {
				transaction.rollback();
				flag = true;
				response.setMsg(e.getMessage());
				response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		if (!flag) {
			if (productTrialId != null) {
				response.setProductTrialId(productTrialId);
				response.setMsg("Product Trial is created Successful.");
				response.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				response.setMsg("Error While Creating Product Trial");
				response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			
		}
		
		
		return response;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		NativeQuery<?> query = null;
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
			ex.printStackTrace();
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			ex.printStackTrace();
		} finally {
		}
		return mapData;
	}


}
