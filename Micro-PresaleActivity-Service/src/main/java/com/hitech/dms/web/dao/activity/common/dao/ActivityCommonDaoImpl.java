/**
 * 
 */
package com.hitech.dms.web.dao.activity.common.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimDtlListResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanHDRResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityCommonDaoImpl implements ActivityCommonDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityCommonDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ActivityClaimHdrResponseModel fetchActivityPlanHdr(String userCode, BigInteger activityPlanHdrId, String isFor,
			Device device) {
		Session session = null;
		ActivityClaimHdrResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchActivityPlanHdr(session, userCode, activityPlanHdrId, isFor);

		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings("deprecation")
	public ActivityClaimHdrResponseModel fetchActivityPlanHdr(Session session, String userCode,
			BigInteger activityPlanHdrId, String isFor) {
		NativeQuery<?> query = null;
		ActivityClaimHdrResponseModel apHdr = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYPLAN_DTL] :userCode, :activityPlanHdrId, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("activityPlanHdrId", activityPlanHdrId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					apHdr = new ActivityClaimHdrResponseModel();
					apHdr.setActivityPlanHdrId((BigInteger) row.get("activity_plan_hdr_id"));
					apHdr.setActivityPlanDate((String) row.get("activity_creation_date"));
					apHdr.setProfitCenter((String) row.get("profit_center"));
					apHdr.setFiannncialYear((Integer) row.get("activity_year"));
					apHdr.setFiannncialMonth((Integer) row.get("activity_month"));
					apHdr.setFinancialMonthDesc((String) row.get("activityMonth"));
					apHdr.setActivityNo((String) row.get("activityNo"));
					apHdr.setActivityStatus((String) row.get("activity_status"));
					apHdr.setSeriesName((String) row.get("series_name"));
					apHdr.setSegmentName((String) row.get("segment_name"));
					apHdr.setPcId((Integer) row.get("pc_id"));
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return apHdr;
	}
	
	public ActivityPlanHDRResponseModel fetchActivityPlanHDRDTL(String userCode,
			BigInteger activityPlanHdrId, String isFor) {
		ActivityPlanHDRResponseModel responseModel = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchActivityPlanHDRDTL(session, userCode, activityPlanHdrId, isFor);
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
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActivityPlanHDRResponseModel fetchActivityPlanHDRDTL(Session session, String userCode,
			BigInteger activityPlanHdrId, String isFor) {
		ActivityPlanHDRResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYPLAN_DTL] :userCode, :activityPlanHdrId, :isFor";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("activityPlanHdrId", activityPlanHdrId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModel = new ActivityPlanHDRResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setActivityPlanHdrId((BigInteger) row.get("activity_plan_hdr_id"));
					responseModel.setActivityNo((String) row.get("activityNo"));
					responseModel.setActivityCreationDate((String) row.get("activity_creation_date"));
					responseModel.setActivityMonth((Integer) row.get("activity_month"));
					responseModel.setActivityMonthName((String) row.get("activityMonth"));
					responseModel.setActivityYear((Integer) row.get("activity_year"));
					responseModel.setActivityStatus((String) row.get("activity_status"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setSegmentName((String) row.get("segment_name"));
					responseModel.setPcId((Integer) row.get("pc_id"));
					responseModel.setPcDesc((String) row.get("profit_center"));
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
		return responseModel;
	}
	
	@SuppressWarnings("deprecation")
	public List<ActivityClaimDtlListResponseModel> fetchActivityClaimDtlList(Session session, String userCode,
			BigInteger dealerId, BigInteger activityPlanHdrId,String isFor) {
		NativeQuery<?> query = null;
		ActivityClaimDtlListResponseModel apDtl = null;
		List<ActivityClaimDtlListResponseModel> apDtlList = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYNOS_LIST] :userCode,:dealerId, :activityPlanHdrId, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);
			query.setParameter("activityPlanHdrId", activityPlanHdrId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				apDtlList = new ArrayList<ActivityClaimDtlListResponseModel>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					apDtl = new ActivityClaimDtlListResponseModel();
					apDtl.setActivityActualHdrId((BigInteger) row.get("ActivityActualHdr_ID"));
					apDtl.setActivityActualNo((String) row.get("ActivityActualNo"));
					apDtl.setActivityName((String) row.get("ActivityName"));
					apDtl.setActivityLocation((String) row.get("ActivityLocation"));
					apDtl.setActivityClaimDtlId((BigInteger)row.get("claimDetailId"));
					apDtl.setTotalEnquiries((Integer) row.get("TotalEnquries"));
					apDtl.setTotalBudget((BigDecimal) row.get("TotalBudget"));
					apDtl.setApprovedBudget((BigDecimal) row.get("ApprovedBudgetAmount"));
					BigDecimal revisedApprovedBudget=(BigDecimal) row.get("revised_approved_amt");
					apDtl.setRevisedApprovedBudget(revisedApprovedBudget!=null?revisedApprovedBudget:BigDecimal.ZERO);
					apDtl.setReimbursementClaim((BigDecimal) row.get("reimbursement_claim"));
					apDtl.setVendorInvoiceNo((String) row.get("vendor_invoice_no"));
					apDtl.setVendorInvoiceAmount((BigDecimal) row.get("vendor_invoice_amount"));
					apDtl.setApprovedClaimAmount((BigDecimal) row.get("approved_claim_amount"));
					apDtl.setVendorInvoiceFile((String) row.get("vendor_invoice_file"));
					apDtl.setActiityPhotoFile((String) row.get("actiity_photo_file"));
					apDtl.setApproverRemarks((String) row.get("approver_remarks"));
					apDtl.setGlCode((String)row.get("GL_CODE"));
					apDtl.setHsnCode((String)row.get("HSN_Code"));
					apDtlList.add(apDtl);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		//System.out.println("before Sending list "+apDtlList);
		return apDtlList;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchApprovalData(Session session, String approvalCode) {
		String sqlQuery = "select approver_level_seq, designation_level_id, grp_seq_no, 'Pending at '+dl.DesignationLevelDesc as approvalStatus,"
				+ "       isFinalApprovalStatus" + "       from SYS_APPROVAL_FLOW_MST(nolock) sf"
				+ "       inner join ADM_HO_MST_DESIG_LEVEL(nolock) dl on sf.designation_level_id=dl.ho_designation_level_id"
				+ "       where transaction_name=:approvalCode"
				+ "       order by approver_level_seq,grp_seq_no";
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
	@SuppressWarnings("deprecation")
	public ActivityClaimHdrResponseModel fetchActivityClaimHdr(Session session, String userCode,
			BigInteger id,String isFor) {
		NativeQuery<?> query = null;
		ActivityClaimHdrResponseModel apHdr = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYPLAN_DTL] :userCode, :id,:isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("id", id);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					apHdr = new ActivityClaimHdrResponseModel();
					apHdr.setDealerName((String) row.get("dealerName"));
					apHdr.setActivityPlanHdrId((BigInteger) row.get("activity_plan_hdr_id"));
					apHdr.setActivityClaimDate((String) row.get("activity_claim_date"));
					apHdr.setActivityClaimNumber((String) row.get("activity_claim_number"));
					apHdr.setActivityClaimInvId((BigInteger) row.get("activity_claim_inv_hdr_id"));
					apHdr.setActivityClaimInvNumber((String) row.get("claim_invoice_no"));
					apHdr.setActivityNo((String) row.get("activityNo"));
					apHdr.setActivityPlanDate((String) row.get("activity_creation_date"));
					apHdr.setFiannncialYear((Integer) row.get("activity_year"));
					apHdr.setFiannncialMonth((Integer) row.get("activity_month"));
					apHdr.setFinancialMonthDesc((String) row.get("activityMonth"));
					apHdr.setProfitCenter((String) row.get("profit_center"));
					apHdr.setSeriesName((String) row.get("series_name"));
					apHdr.setSegmentName((String) row.get("segment_name"));
					apHdr.setPcId((Integer) row.get("pc_id"));
					apHdr.setActivityStatus((String) row.get("activity_status"));
					apHdr.setTotalClaimAmount((BigDecimal) row.get("total_claim_amount"));
					apHdr.setActivityClaimHdrId((BigInteger) row.get("activity_claim_hdr_id"));
					apHdr.setTotalApprovedAmount((BigDecimal) row.get("total_approved_amount"));
					apHdr.setApproverFinalRemarks((String) row.get("approver_final_remarks"));
					apHdr.setGstPer((BigDecimal) row.get("GST_Per"));
					apHdr.setGstAmount((BigDecimal) row.get("GST_AMOUNT"));
					apHdr.setTotalInvoiceAmount((BigDecimal) row.get("TOTAL_INVOICE_AMOUNT"));
					apHdr.setCustomerInvoiceNumber((String) row.get("customer_invoice_no"));
					apHdr.setCustomerInvoiceDate((String) row.get("customer_invoice_date"));
					apHdr.setFinalSubmit((String) row.get("final_submit"));
					
					
					
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return apHdr;
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
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchBranchDetials(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		NativeQuery<?> query = null;
		String sqlQuery = "Select * from [FN_CM_GETBRANCH_BYUSERCODE] (:userCode, :isInactiveInclude)";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isInactiveInclude", 'N');
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger branchId = null;
				for (Object object : data) {
					Map row = (Map) object;
					branchId = (BigInteger) row.get("BRANCH_ID");
				}
				mapData.put("branchId", branchId);
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
