/**
 * 
 */
package com.hitech.dms.web.dao.common;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.dc.common.response.CancelReasonResponseModel;
import com.hitech.dms.web.model.dc.common.response.DcTypeResponModel;
import com.hitech.dms.web.model.inv.common.response.InvoiceTypeResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class CommonDaoImpl implements CommonDao {
	private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> updateEnquiryStatus(Session session, String userCode, BigInteger userId, Date currDate,
			BigInteger enquiryHdrId, String status) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateEnquiryStatus invoked.." + userCode);
		}
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Update SA_ENQ_HDR Set enquiry_status =:status, ModifiedBy=:userId, ModifiedDate=:currDate where enquiry_id =:enqHdrId";
		mapData.put("ERROR", "ERROR WHILE UPDATING ENQUIRY STATUS.");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("userId", userId);
			query.setParameter("currDate", currDate);
			query.setParameter("enqHdrId", enquiryHdrId);
			int k = query.executeUpdate();
			if (k > 0) {
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE UPDATING ENQUIRY STATUS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE UPDATING ENQUIRY STATUS.");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> updateAllotmentStatus(Session session, String userCode, BigInteger userId, Date currDate,
			BigInteger allotmentHdrId, String status) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateAllotmentStatus invoked.." + userCode);
		}
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Update SA_MACHINE_ALLOTMENT Set allotment_status =:status, last_modified_by=:userId, last_modified_date=:currDate where machine_allotment_id =:allotmentHdrId";
		mapData.put("ERROR", "ERROR WHILE UPDATING ALLOTMENT STATUS.");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("userId", userId);
			query.setParameter("currDate", currDate);
			query.setParameter("allotmentHdrId", allotmentHdrId);
			int k = query.executeUpdate();
			if (k > 0) {
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE UPDATING ALLOTMENT STATUS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE UPDATING ALLOTMENT STATUS.");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select HO.ho_usr_id from ADM_HO_USER (nolock) HO INNER JOIN ADM_USER (nolock) u ON u.ho_usr_id = HO.ho_usr_id where u.UserCode =:userCode";
		mapData.put("ERROR", "HO USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("ho_usr_id");
				}
				mapData.put("hoUserId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id, dlr_emp_id, ho_usr_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("userId", (BigInteger) row.get("user_id"));
					mapData.put("dlrEmpId", (BigInteger) row.get("dlr_emp_id"));
					mapData.put("hoUserId", (BigInteger) row.get("ho_usr_id"));
				}
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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchApprovalData(Session session, String approvalCode) {
		String sqlQuery = "select approver_level_seq, designation_level_id, grp_seq_no, 'Pending at '+dl.DesignationLevelDesc as approvalStatus,"
				+ "       isFinalApprovalStatus" + "       from SYS_APPROVAL_FLOW_MST(nolock) sf"
				+ "       inner join ADM_HO_MST_DESIG_LEVEL(nolock) dl on sf.designation_level_id=dl.ho_designation_level_id"
				+ "       where transaction_name=:approvalCode" + "       order by approver_level_seq,grp_seq_no";
		List data = null;
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("approvalCode", approvalCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return data;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<DcTypeResponModel> fetchDcTypeList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDcTypeList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<DcTypeResponModel> dcTypeList = null;
		String sqlQuery = "Select * from SA_MST_MACHINE_DC_TYPE (nolock) where isActive = 'Y' order by sequence_no";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dcTypeList = new ArrayList<DcTypeResponModel>();
				for (Object object : data) {
					Map row = (Map) object;
					DcTypeResponModel model = new DcTypeResponModel();
					model.setDcTypeId((Integer) row.get("dc_type_id"));
					model.setDcTypeCode((String) row.get("dc_type_code"));
					model.setDcTypeDesc((String) row.get("dc_type_desc"));

					dcTypeList.add(model);
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
		return dcTypeList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<InvoiceTypeResponseModel> fetchInvoiceTypeList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceTypeList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<InvoiceTypeResponseModel> invTypeList = null;
		String sqlQuery = "exec [SP_SA_INV_TYPE_LIST] :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				invTypeList = new ArrayList<InvoiceTypeResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					InvoiceTypeResponseModel model = new InvoiceTypeResponseModel();
					model.setInvoiceTypeId((Integer) row.get("invoice_type_id"));
					model.setInvoiceTypeCode((String) row.get("invoice_type_code"));
					model.setInvoiceTypeDesc((String) row.get("invoice_type_name"));
					model.setApplicableToDealer((String) row.get("applicableToDealer"));
					model.setApplicableToDistributor((String) row.get("applicableToDistributor"));

					invTypeList.add(model);
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
		return invTypeList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public InvoiceTypeResponseModel fetchInvoiceTypeDtl(Session session, String userCode, Integer invoiceTypeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceTypeDtl invoked.." + userCode);
		}
		Query query = null;
		InvoiceTypeResponseModel model = null;
		String sqlQuery = "Select * from SA_MST_MACHINE_INVOICE_TYPE (nolock) where invoice_type_id =:invoiceTypeId";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("invoiceTypeId", invoiceTypeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					model = new InvoiceTypeResponseModel();
					model.setInvoiceTypeId((Integer) row.get("invoice_type_id"));
					model.setInvoiceTypeCode((String) row.get("invoice_type_code"));
					model.setInvoiceTypeDesc((String) row.get("invoice_type_name"));
					model.setApplicableToDealer((String) row.get("applicableToDealer"));
					model.setApplicableToDistributor((String) row.get("applicableToDistributor"));

				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return model;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<CancelReasonResponseModel> fetchCancelReasonList(String userCode, String code) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCancelReasonList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<CancelReasonResponseModel> responseModelList = null;
		String sqlQuery = "select * from SA_MST_MACHINE_CANCEL_REASON (nolock) where isActive = 'Y' and cancel_reason_type =:code order by cancel_reason_desc ";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("code", code);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CancelReasonResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					CancelReasonResponseModel model = new CancelReasonResponseModel();
					model.setCancelReasonId((Integer) row.get("cancel_reason_id"));
					model.setCancelReasonType((String) row.get("cancel_reason_type"));
					model.setCancelReasonDesc((String) row.get("cancel_reason_desc"));

					responseModelList.add(model);
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
		return responseModelList;
	}

	@Override
	public String getDocumentNumber(String documentPrefix, Integer branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);

		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}

	@Override
	public String getDocumentNumberById(String documentPrefix, BigInteger branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);

		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}

	@Override
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session) {
		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC [UpdateDocumentNo] :lastDocumentNo,:documentPrefix,:branchId,:currentDate";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("documentPrefix", documentPrefix);
			query.setParameter("branchId", Integer.parseInt(branchId));
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
		
	}
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public void updateDocumentNumber(String documentType, BigInteger branchID, String pcrNo, Session session) {
		Query query = null;
        Transaction transaction = session.beginTransaction();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate = formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'" + currentDate
				+ "',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", pcrNo);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchID);
		// query.setParameter("DocumentType", documentType);

		int k = query.executeUpdate();
		transaction.commit();

		
	}
}
