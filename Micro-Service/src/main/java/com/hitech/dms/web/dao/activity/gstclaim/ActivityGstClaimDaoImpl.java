/**
 * 
 */
package com.hitech.dms.web.dao.activity.gstclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.DozerBeanMapper;
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
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.activityGstClaim.ActivityGstClaimInvoiceApprovalEntity;
import com.hitech.dms.web.entity.activityGstClaim.ActivityGstClaimInvoiceHdrEntity;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimGstInvSearchRequest;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvoiceRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvSearchResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvoiceResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimInvApprovalResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityCreditNoteResponseModel;

/**
 * @author santosh.kumar
 *
 */
@Repository
public class ActivityGstClaimDaoImpl implements ActivityGstClaimDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityGstClaimDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private DocumentNumberGenerator docNumber;
	

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public Map<String, Object> fetchActivityGstClaimList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityHeader invoked..", userCode);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_GET_ACTIVITY_CLAIM_NUMBER] :userCode ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
		    query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("ActivityClaimList", data);

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
		return responseListModel;
	}

	@Override
	public Map<String, Object> fetchActivityHeader(String userCode, Integer activityClaimId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityHeader invoked.." + userCode + "::::::::::activityClaimId::::::::::"
					+ activityClaimId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_ACTIVITY_DETAILS_FOR_GST_INVOICE] :ClaimId,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("UserCode", userCode);
			query.setParameter("ClaimId", activityClaimId);
			query.setParameter("FLAG", "Header");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("Header", data);

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
		return responseListModel;
	}

	@Override
	public Map<String, Object> fetchActivityClaimDetails(String userCode, Integer activityClaimId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimDetails invoked.." + userCode + "::::::::::activityClaimId::::::::::"
					+ activityClaimId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_ACTIVITY_DETAILS_FOR_GST_INVOICE] :ClaimId,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("UserCode", userCode);
			query.setParameter("ClaimId", activityClaimId);
			query.setParameter("FLAG", "Details");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("Details", data);

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
		return responseListModel;
	}

	
	@Override
	public ActivityClaimGstInvoiceResponseModel updateActivityGstClaimInvoice(String userCode,
			@Valid ActivityClaimInvoiceRequestModel requestedData, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateActivityClaimInvoice invoked..");
		}
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		ActivityClaimGstInvoiceResponseModel responseModel = new ActivityClaimGstInvoiceResponseModel();
		
		boolean isSuccess = true;
		Map<String, Object> branchDetails = null;
		String msg = null;
		String activityGstClaimNo = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			branchDetails=fetchBranchDetials(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				
				String sqlQuery = "update SV_ACTIVITY_CLAIM_INVOICE_HDR set customer_invoice_no=:custInvNo,customer_invoice_date=:custInvDate,final_submit=:finalSub where activity_claim_inv_hdr_id =:idClaim";
	            try {
	            	Query query = null;
	            	query = session.createNativeQuery(sqlQuery);
	            	query.setParameter("custInvNo", requestedData.getCustomerInvoiceNo());
	    			query.setParameter("custInvDate", requestedData.getCustomerInvoiceDate());
	    			query.setParameter("finalSub", requestedData.getFinalSubmitFlag());
	    			query.setParameter("idClaim", requestedData.getActivityClaimHdrId());
	    			
	    			int k = query.executeUpdate();
	            }
	            catch (SQLGrammarException ex) {
	    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
	    			logger.error(this.getClass().getName(), ex);
	    		} catch (Exception ex) {
	    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
	    			logger.error(this.getClass().getName(), ex);
	    		}
				
				transaction.commit();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Activity Claim Invoice Created Successfully";
				responseModel.setMsg(msg);
				
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
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
	public ActivityClaimGstInvoiceResponseModel createActivityGstClaimInvoice(String userCode,
			@Valid ActivityClaimInvoiceRequestModel requestedData, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createActivityClaimInvoice invoked..");
		}
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		ActivityClaimGstInvoiceResponseModel responseModel = new ActivityClaimGstInvoiceResponseModel();
		ActivityGstClaimInvoiceHdrEntity activityClaimInvoiceHdrEntity = null;
		boolean isSuccess = true;
		Map<String, Object> branchDetails = null;
		String msg = null;
		String activityGstClaimNo = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			branchDetails=fetchBranchDetials(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				activityClaimInvoiceHdrEntity = mapper.map(requestedData, ActivityGstClaimInvoiceHdrEntity.class,
						"ActivityGstClaimCreateResMapId");
				//BigInteger branchId = BigInteger.valueOf(3);
				
				BigInteger branchId = null;
				branchId = (BigInteger) branchDetails.get("branchId");
				System.out.println("branchId:::::::"+branchId);
				activityGstClaimNo = getDocumentNumber("ACGI", branchId.intValue(), session);
				System.out.println("activityGstClaimNo:::::::"+activityGstClaimNo);
				//saveActivityPlanNumberInCmDocBranch("Activity Claim Gst Invoice", branchId.intValue(), activityGstClaimNo, session);
				updateDocumentNumber(activityGstClaimNo.substring(activityGstClaimNo.length() - 7), "ACGI",branchId + "", session,"Activity Claim Gst Invoice");
				activityClaimInvoiceHdrEntity.setClaimInvoiceNo(activityGstClaimNo);
				//System.out.println("activityClaimInvoiceHdrEntity"+activityClaimInvoiceHdrEntity);
				activityClaimInvoiceHdrEntity.setClaimInvoiceDate(new Date());
				activityClaimInvoiceHdrEntity.setClaimInvoiceStatus("Pending");
				if(activityGstClaimNo != null){
					session.save(activityClaimInvoiceHdrEntity);
				}
				
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess && activityGstClaimNo != null) {
				mapData = saveIntoApproval(session, userId, null,
						activityClaimInvoiceHdrEntity.getActivityClaimInvHdrId(),
						activityClaimInvoiceHdrEntity.getTotalInvoiceAmnt());
				if (mapData != null && mapData.get("SUCCESS") != null && activityGstClaimNo!=null) {
					transaction.commit();
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					msg = "Activity Claim Invoice Created Successfully";
					responseModel.setMsg(msg);
				} else {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("Error While Inserting Into Claim Invoice Approval Tables.");
				}
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
			if (isSuccess && activityGstClaimNo != null) {
				mapData = fetchActivityClaimInvNoByActivityClaimInvId(
						activityClaimInvoiceHdrEntity.getActivityClaimInvHdrId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setActivityClaimGstInvoiceNumber((String) mapData.get("activityClaimInvNo"));
				}
			}else if (activityGstClaimNo != null) {
                responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
                responseModel.setStatusCode(500);
            } else {
                responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
            }
			
		}
		return responseModel;
	}
	
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session,String docDesc) {

		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC Update_INV_Doc_No :lastDocumentNo,:DocumentTypeDesc,:currentDate,:branchId,:documentPrefix";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("DocumentTypeDesc", docDesc);
			query.setParameter("documentPrefix", documentPrefix);
			query.setParameter("branchId", Integer.parseInt(branchId));
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}	
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
	

//	public String getDocumentNumber(String documentPrefix, BigInteger branchId) {
//	    Session session = sessionFactory.openSession();
//	    String documentNumber = null;
//	    List data = null;
//	    try {
//	    	System.out.println(documentPrefix + branchId );
//	        String dateToString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//	        String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
//	        Query<?> query = session.createSQLQuery(sqlQuery)
//	                                .setParameter("DocumentType", documentPrefix)
//	                                .setParameter("BranchID", branchId)
//	                                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//	        data = query.list();
//
//	        System.out.println("data"+data);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    } finally {
//	        session.close();
//	    }
//
//	    
//	    return documentNumber;
//	}

	
	
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

	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger activityClaimInvId, BigDecimal approvedAmount) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO ACTIVITY CLAIM INVOICE APPROVAL TABLE.");
		try {
			List data = fetchApprovalData(session, "SV_ACTIVITY_GST_CLAIM_INVOICE_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					ActivityGstClaimInvoiceApprovalEntity activityClaimInvApprovalEntity = new ActivityGstClaimInvoiceApprovalEntity();
					activityClaimInvApprovalEntity.setActivityClaimInvHdrId(activityClaimInvId);
					activityClaimInvApprovalEntity.setAppLevelSeq((Integer) row.get("approver_level_seq"));
					activityClaimInvApprovalEntity.setApprovalStatus((String) row.get("approvalStatus"));
//					activityClaimInvApprovalEntity.setApprovedDate(new Date());
					activityClaimInvApprovalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					activityClaimInvApprovalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						activityClaimInvApprovalEntity.setIsFinalApprovalStatus(true);
					} else {
						activityClaimInvApprovalEntity.setIsFinalApprovalStatus(false);
					}
					activityClaimInvApprovalEntity.setRejectedFlag(false);
					activityClaimInvApprovalEntity.setHoUserId(null);

					activityClaimInvApprovalEntity.setApprovedAmount(approvedAmount);

					session.save(activityClaimInvApprovalEntity);
					
					
				}
			}
			mapData.put("SUCCESS", "Inserted Into Activity Approval Table.");
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
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
				+ "       where transaction_name= :approvalCode " + "order by approver_level_seq,grp_seq_no";
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActivityClaimInvNoByActivityClaimInvId(BigInteger activityClaimInvId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select claim_invoice_no from SV_ACTIVITY_CLAIM_INVOICE_HDR (nolock) sacih where sacih.activity_claim_inv_hdr_id =:activityClaimInvId";
		mapData.put("ERROR", "Activity Claim Invoice Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityClaimInvId", activityClaimInvId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String activityClaimInvNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					activityClaimInvNo = (String) row.get("claim_invoice_no");
				}
				mapData.put("activityClaimInvNo", activityClaimInvNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY CLAIM INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY CLAIM INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private void saveActivityPlanNumberInCmDocBranch(String documentType, BigInteger branchId, String activityPlanNo,
			Session session) {
		Query query = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate = formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'" + currentDate
				+ "',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", activityPlanNo);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchId);
//		query.setParameter("DocumentType", documentType);

		int k = query.executeUpdate();

	}

	@Override
	public List<ActivityClaimGstInvSearchResponseModel> fetchActivityClaimGstInvSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimInvSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActivityClaimGstInvSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SV_SEARCH_ACTIVITYCLAIM_GST_INVOICE] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :activityClaimInvoiceNumber, :fromDate, :toDate, :isIncludeInActive, :page, :size, :plantId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("orgHierId", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("activityClaimInvoiceNumber", requestModel.getActivityClaimInvoiceNumber());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: requestModel.getFromDate()));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: requestModel.getToDate()));
			query.setParameter("isIncludeInActive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setParameter("plantId", requestModel.getPlantCode()); //added on 12-09-24
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityClaimGstInvSearchResponseModel>();
				ActivityClaimGstInvSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityClaimGstInvSearchResponseModel();
					responseModel.setSrlNo((BigInteger) row.get("Sr_No"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setId((BigInteger) row.get("ActivityClaimInvId"));
					responseModel.setActivityClaimInvNumber((String) row.get("ActivityClaimInvNumber"));
					responseModel.setClaimInvoiceDate((String) row.get("ClaimInvoiceDate"));
					responseModel.setActivityPlanNo((String) row.get("ActivityPlanNo"));
					responseModel.setActivityPlanDate((String) row.get("ActivityPlanDate"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setProfitCenterDesc((String) row.get("ProfitCenterDesc"));
//					responseModel.setFinancialYear((Integer) row.get("FinancialYear"));
//					responseModel.setFinancialMonth((String) row.get("FinancialMonth"));
					responseModel.setTotalBudget((BigDecimal) row.get("TotalBudget"));
					responseModel.setTotalApprovedBudget((BigDecimal) row.get("TotalApprovedBudget"));
					responseModel.setTotalReimbursementClaim((BigDecimal) row.get("TotalReimbursementClaim"));
					responseModel.setApprovedClaimAmount((BigDecimal) row.get("ApprovedClaimAmount"));
					responseModel.setGstPer((BigDecimal) row.get("GstPer"));
					responseModel.setGstAmount((BigDecimal) row.get("GstAmount"));
					responseModel.setTotalInvoiceAmount((BigDecimal) row.get("TotalInvoiceAmount"));
					String isReceivedHardCopy = (String) row.get("IsReceivedHardCopy");
					if (isReceivedHardCopy != null && isReceivedHardCopy.equals("Y")) {
						responseModel.setReceivedHardCopy(true);
					} else {
						responseModel.setReceivedHardCopy(false);
					}
					responseModel.setPlantCode((String)row.get("plantCode"));//added on 12-09-24
					responseModel.setPlantName((String)row.get("plantName"));//added on 12-09-24
//					responseModel.setCreditNoteNumber((String) row.get("CreditNoteNumber"));
//					responseModel.setCreditNoteDate((String) row.get("CreditNoteDate"));
//					responseModel.setCreditNoteAmount((BigDecimal) row.get("CreditNoteAmount"));
//					String isCreditNotePdfLink = (String) row.get("IsCreditNotePdfLink");
//					if (isCreditNotePdfLink != null && isCreditNotePdfLink.equals("Y")) {
//						responseModel.setCreditNotePdfLink(true);
//					} else {
//						responseModel.setCreditNotePdfLink(false);
//					}
					responseModel.setFinalSubmit("Y".equalsIgnoreCase((String)row.get("final_submit"))?"Submitted":"Pending");
					
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

	@Override
	public Map<String, Object> fetchActivityClaimGstHeader(String userCode, Integer activityClaimGstinvoiceId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimGstHeader invoked.." + userCode + "::::::::::activityClaimGstinvoiceId::::::::::"
					+ activityClaimGstinvoiceId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_ACTIVITY_CLAIM_GST_INVOICE_FOR_VIEW] :ClaimGstInvoiceId,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("UserCode", userCode);
			query.setParameter("ClaimGstInvoiceId", activityClaimGstinvoiceId);
			query.setParameter("FLAG", "Header");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("Header", data);

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
		return responseListModel;
	}

	@Override
	public Map<String, Object> fetchActivityClaimGstDetails(String userCode, Integer activityClaimGstinvoiceId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimGstDetails invoked.." + userCode + "::::::::::activityClaimGstinvoiceId::::::::::"
					+ activityClaimGstinvoiceId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_ACTIVITY_CLAIM_GST_INVOICE_FOR_VIEW] :ClaimGstInvoiceId,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("UserCode", userCode);
			query.setParameter("ClaimGstInvoiceId", activityClaimGstinvoiceId);
			query.setParameter("FLAG", "Details");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("Details", data);

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
		return responseListModel;
	}

	@Override
	public ActivityClaimInvApprovalResponseModel approveRejectActivityClaimGstInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		ActivityClaimInvApprovalResponseModel responseModel = new ActivityClaimInvApprovalResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "exec [SV_ACTIVITY_CLAIM_GST_INVOICE_APPROVAL] :userCode, :hoUserId, :activityClaimInvHdrId, :approvalStatus, :remark";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("activityClaimInvHdrId", requestModel.getActivityClaimInvHdrId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remark", requestModel.getRemarks());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map row = (Map) object;
							msg = (String) row.get("msg");
							responseModel.setMsg(msg);
							approvalStatus = (String) row.get("approvalStatus");
							responseModel.setApprovalStatus(approvalStatus);
						}
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Error While Validating Claim Invoice Approval.");
					}
				} else {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("HO User Not Found.");
				}
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setApprovalStatus(approvalStatus);
				responseModel.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
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

	@Override
	public List<ActivityCreditNoteResponseModel> fetchActivityCreditNoteSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityCreditNoteSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActivityCreditNoteResponseModel> responseModelList = null;
		String sqlQuery = "exec [SV_SEARCH_ACTIVITY_CREDIT_NOTE] :UserCode,:activityClaimInvNumber, :FromDate, :ToDate, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("activityClaimInvNumber", requestModel.getActivityClaimInvoiceNumber());
			query.setParameter("FromDate", (requestModel.getFromDate() != null ? requestModel.getFromDate()
					: null));
			query.setParameter("ToDate", (requestModel.getToDate() != null ?requestModel.getToDate():null));
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityCreditNoteResponseModel>();
				ActivityCreditNoteResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityCreditNoteResponseModel();
					responseModel.setSrlNo((BigInteger) row.get("Sr_No"));
					//responseModel.setActivityClaimInvId((BigInteger) row.get("ActivityClaimInvId"));
					responseModel.setActivityClaimInvNumber((String) row.get("ActivityClaimInvNumber"));
					responseModel.setClaimInvoiceDate((String) row.get("ClaimInvoiceDate"));
					responseModel.setActivityPlanNo((String) row.get("ActivityPlanNo"));
					responseModel.setActivityPlanDate((String) row.get("ActivityPlanDate"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setProfitCenterDesc((String) row.get("ProfitCenterDesc"));
					responseModel.setFinancialYear((Integer) row.get("FinancialYear"));
					responseModel.setMonth((Integer) row.get("FinancialMonth"));
					responseModel.setTotalBudget((BigDecimal) row.get("TotalBudget"));
					responseModel.setTotalApprovedBudget((BigDecimal) row.get("TotalApprovedBudget"));
					responseModel.setTotalReimbursementClaim((BigDecimal) row.get("TotalReimbursementClaim"));
					responseModel.setApprovedClaimAmount((BigDecimal) row.get("ApprovedClaimAmount"));
					
					//responseModel.setTotalInvoiceAmount((BigDecimal) row.get("TotalInvoiceAmount"));
	
					responseModel.setCreditNoteNumber((String) row.get("CreditNoteNumber"));
					responseModel.setCreditNoteDate((String) row.get("CreditNoteDate"));
					responseModel.setCreditNoteAmount((BigDecimal) row.get("CreditNoteAmount"));
					String isCreditNotePdfLink = (String) row.get("IsCreditNotePdfLink");
					if (isCreditNotePdfLink != null && isCreditNotePdfLink.equals("Y")) {
						responseModel.setCreditNotePdfLink(true);
					} else {
						responseModel.setCreditNotePdfLink(false);
					}
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

	@Override
	public Map<String, Object> fetchActivityClaimGstInvoiceList(String userCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimGstInvoiceList invoked..", userCode);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_GET_ACTIVITY_CLAIM_INVOICE_NUMBER] :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("ActivityGstClaimList", data);

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
		return responseListModel;
	}

	@Override
	public Map<String, Object> fetchActivityClaimGstInvByPlanId(BigInteger activityPlanHdrId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimGstInvByPlanId invoked.." + activityPlanHdrId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "select ac.claim_invoice_no from SV_ACTIVITY_CLAIM_INVOICE_HDR ac WHERE ac.activity_plan_hdr_id = :activityPlanHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("activityPlanHdrId", activityPlanHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("list", data);

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
		return responseListModel;
	}
	
	
	@Override
	public Map<String, Object> getPlantCodeList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityHeader invoked..", userCode);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_GET_PLANT_CODE]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("PlantCodeList", data);

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
		return responseListModel;
	}
}
