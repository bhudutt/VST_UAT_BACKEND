package com.hitech.dms.web.dao.paymentReceipt.create;

import java.math.BigDecimal;
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
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.enquiry.create.EnquiryCreateDaoImpl;
import com.hitech.dms.web.entity.paymentReceipt.PaymentReceiptEntity;
import com.hitech.dms.web.model.paymentReceipt.create.response.EnquiryNoAutoSearchResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptCreateResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptModeResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptTypeResponseModel;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class PaymentReceiptDaoImpl implements PaymentReceiptCreateDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentReceiptDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PaymentReceiptCreateResponseModel createPaymentRecepit(String userCode, PaymentReceiptEntity entitydata, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("Payment Receipt invoked.." + userCode);
			logger.debug(entitydata.toString());
		}
		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PaymentReceiptEntity response = null;
		BigInteger userId = null;

		Date currDate = new Date();
		BigInteger digitalEnqHdrId = null ;
		
		PaymentReceiptCreateResponseModel responseModel =new PaymentReceiptCreateResponseModel();
		try {
			response = new PaymentReceiptEntity();
			session = sessionFactory.openSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			transaction = session.beginTransaction();
			entitydata.setReceiptNo("TPN");
			entitydata.setLastModifiedBy(userId);
			entitydata.setLastModifiedDate(currDate);
			digitalEnqHdrId = (BigInteger) session.save(entitydata);
			transaction.commit();
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			if (transaction.isActive())
				transaction.rollback();
			flag = true;
			responseModel.setMsg(e.getMessage());
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		} finally {
			if (session.isOpen())
				session.close();
		}
		if (!flag) {
			if (digitalEnqHdrId != null) {
				responseModel.setPaymentReceiptId(digitalEnqHdrId);
				responseModel.setMsg("Payment Receipt is created Successful.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				responseModel.setMsg("Error While Creating Payment Receipt.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public List<PaymentReceiptModeResponseModel> getPaymentReceiptMode(String userCode) {
		Session session = null;
		List<PaymentReceiptModeResponseModel> pcModeList = null;
		PaymentReceiptModeResponseModel responseModel = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_FM_GetPAYMENTRECIEPT_MODE]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				pcModeList = new ArrayList<PaymentReceiptModeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new PaymentReceiptModeResponseModel();
					responseModel.setReceipt_mode_id((Integer) row.get("receipt_mode_id"));
					responseModel.setReceipt_mode((String) row.get("receipt_mode"));
					pcModeList.add(responseModel);
				}
			}
		} catch (SQLGrammarException sqlge) {
			logger.error(this.getClass().getName(), sqlge);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return pcModeList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<PaymentReceiptTypeResponseModel> getPaymentReceiptType(String userCode, BigInteger enquiryId) {
		Session session = null;
		List<PaymentReceiptTypeResponseModel> pcTypeList = null;
		PaymentReceiptTypeResponseModel responseModel = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_FM_GetPAYMENTRECIEPT_TYPE] :enquiryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enquiryId", enquiryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				pcTypeList = new ArrayList<PaymentReceiptTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new PaymentReceiptTypeResponseModel();
					responseModel.setReceiptType((String) row.get("receiptType"));;
					responseModel.setAmount((Double) row.get("amount"));
					responseModel.setReceipt_type_id((Integer) row.get("receipt_type_id"));
					pcTypeList.add(responseModel);
				}
			}
		} catch (SQLGrammarException sqlge) {
			logger.error(this.getClass().getName(), sqlge);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return pcTypeList;
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

	@Override
	public List<EnquiryNoAutoSearchResponseModel> enquryNoAutoSearch(String userCode, String enquiryNo, String branchId, String isFor) {
		Session session = null;
		List<EnquiryNoAutoSearchResponseModel> enquiryNoList = null;
		EnquiryNoAutoSearchResponseModel responseModel = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_ENQ_AutoSearch_ByMobile] :EnquiryText, :userCode,:branchId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("EnquiryText", enquiryNo);
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiryNoList = new ArrayList<EnquiryNoAutoSearchResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new EnquiryNoAutoSearchResponseModel();
					responseModel.setEnquiryNo((String) row.get("enquiry_number"));
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					enquiryNoList.add(responseModel);
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
		return enquiryNoList;
	}

	@Override
	public List<PaymentReceiptTypeResponseModel> getReceiptAmtTotalByEnqId(String userCode, BigInteger enquiryId,
			BigInteger receiptTypeId) {
	    	Session session = null;
		List<PaymentReceiptTypeResponseModel> pcTypeList = null;
		PaymentReceiptTypeResponseModel responseModel = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_FM_GetPAYMENTRECIEPT_TYPE_TOTAL_AMT]  :enquiryId, :receiptTypeId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("receiptTypeId", receiptTypeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				pcTypeList = new ArrayList<PaymentReceiptTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new PaymentReceiptTypeResponseModel();
					responseModel.setTotalAmount((BigDecimal) row.get("totalAmt"));
					pcTypeList.add(responseModel);
				}
			}
		} catch (SQLGrammarException sqlge) {
			logger.error(this.getClass().getName(), sqlge);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return pcTypeList;
	}

}
