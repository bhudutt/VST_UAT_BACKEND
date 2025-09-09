/**
 * 
 */
package com.hitech.dms.web.dao.activity.claim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.util.concurrent.AtomicDouble;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.FileUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.activityclaim.ActivityClaimApprovalEntity;
import com.hitech.dms.web.entity.activityclaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaim;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaimSearchRequestModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimSearchListResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimSearchMainResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityPermissionRequestModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;

/**
 * @author santosh.kumar
 *
 */
@Repository
public class ActivityClaimDaoImpl implements ActivityClaimDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimDaoImpl.class);
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

	@SuppressWarnings({ "null", "null" })
	@Override
	public Map<String, Object> getActivityDetails(String userCode, Integer activityPlanId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getActivityDetails invoked.." + userCode + "::::::::::acIntegertivityPlanId::::::::::"
					+ activityPlanId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_Activity_details_For_Claim] :UserCode, :ActivityPlanId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("ActivityPlanId", activityPlanId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("ActivityDetails", data);

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
	public ActivityClaimCreateResponseModel saveActivityClaim(String userCode,
			@Valid ActivityClaimHdrEntity activityClaim, List<MultipartFile> files, Device device) {
		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		BigInteger activityClaimHdrId = null;
		Date currDate = new Date();
		boolean fileFlag = false;
		String activityClaimNo = null;
	    BigInteger aclaimHdrId=null;
		AtomicDouble value = new AtomicDouble(0.0);

		ActivityClaimCreateResponseModel responseModel = new ActivityClaimCreateResponseModel();
		try {
			session = sessionFactory.openSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			transaction = session.beginTransaction();
			// activityClaim.setActivityClaimNumber("ACT");
			activityClaimNo = docNumber.getDocumentNumber1("SAC", activityClaim.getBranchId(), session);
			saveActivityPlanNumberInCmDocBranch("ACIVITY CLAIM", activityClaim.getBranchId(), activityClaimNo, session);

//			activityClaim.setActivityClaimNumber("ACT/" + activityClaim.getActivityPlanHdrId() + "/"
//					+ activityClaim.getDealerId() + "/" + currDate.getTime());
			activityClaim.setActivityClaimNumber(activityClaimNo);
			activityClaim.setActivityClaimStatus(WebConstants.PENDING);
			activityClaim.setCreatedBy(userId);
			activityClaim.setCreatedDate(currDate);
			activityClaim.getActivityClaimDtl().forEach(dtl -> {
				Double reimbursementClaim = dtl.getReimbursementClaim().doubleValue();
				if (reimbursementClaim != null) {
					value.addAndGet(reimbursementClaim);
				}
				dtl.setAcHdr(activityClaim);
				// dtl.setActivityActualHdrId(activityClaim.getActivityActualHdrId());
				if (dtl.getActiityPhotoFile() != null) {
					String[] photo = dtl.getActiityPhotoFile().split(Pattern.quote("\\"));
					dtl.setActiityPhotoFile(photo[photo.length - 1]);
				}
				if (dtl.getVendorInvoiceFile() != null) {
					String[] invoice = dtl.getVendorInvoiceFile().split(Pattern.quote("\\"));
					// String invoice =
					// dtl.getVendorInvoiceFile().substring(dtl.getVendorInvoiceFile().lastIndexOf('\\'));
					dtl.setVendorInvoiceFile(invoice[invoice.length - 1]);
				}

			});
			activityClaim.setTotalReimbursemenAmount(BigDecimal.valueOf(value.get()));
			ActivityClaimApprovalEntity aprrove = null;
			List<ActivityClaimApprovalEntity> aprroveList = null;

			List<?> fetchApprovalData = fetchApprovalData(session, "SV_ACTIVITY_CLAIM_APPROVAL");
			if (fetchApprovalData != null && !fetchApprovalData.isEmpty()) {
				aprroveList = new ArrayList<>();
				for (Object object : fetchApprovalData) {
					Map<?, ?> row = (Map<?, ?>) object;
					aprrove = new ActivityClaimApprovalEntity();
					aprrove.setApprovalStatus((String) row.get("approvalStatus"));
					aprrove.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					aprrove.setIsFinalApprovalStatus((char) row.get("isFinalApprovalStatus"));
					aprrove.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
					aprrove.setAcHdr(activityClaim);
					aprrove.setDesignationLevelId((Integer) row.get("designation_level_id"));
					aprroveList.add(aprrove);
				}
				activityClaim.setActivityClaimApprove(aprroveList);
			}
			if(activityClaimNo != null){
				aclaimHdrId = (BigInteger) session.save(activityClaim);	
			}

			System.out.println("aclaimHdrId" + aclaimHdrId);
			activityClaimHdrId = aclaimHdrId;
			final BigInteger aclaimHdrIds=aclaimHdrId;
			// responseModel.setActivityClaimNumber(activityClaimHdrId);
			responseModel.setActivityClaimNumber(activityClaim.getActivityClaimNumber());
			if (activityClaimHdrId != null) {
				if (files != null && !files.isEmpty()) {

					logger.info("inside file ");
					System.out.println("inside file ");
					try {
						files.forEach(m -> {
							String filePath = fileStorageProperties.getUploadDirMain()
									+ messageSource.getMessage("service.activityClaim.file.upload.dir",
											new Object[] { aclaimHdrIds }, LocaleContextHolder.getLocale());
//							Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m);
							logger.info("inside file " + filePath);
							System.out.println("inside file " + filePath);
							Map<String, String> uploadDOCDTLDOCS = FileUtils.uploadDOCDTLDOCS(filePath, m);
							System.out.println("uploadDOCDTLDOCS  " + uploadDOCDTLDOCS);
							logger.info("uploadDOCDTLDOCS  " + uploadDOCDTLDOCS);
						});
					} catch (Exception e) {
						logger.error(this.getClass().getName(), e);
						fileFlag = true;
					}

				}
			}
			if (!fileFlag &&  activityClaimNo!=null) {
				transaction.commit();
			}

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
			responseModel.setActivityClaimHdrId(activityClaimHdrId);
			mapData = fetchActivityClaimNoByActivityClaimId(activityClaimHdrId);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				responseModel.setActivityClaimNumber((String) mapData.get("activityClaimNo"));
				responseModel.setMsg("Activity Claim is created Successful.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
			
			else if (activityClaimNo != null) {
                responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
                responseModel.setStatusCode(500);
            }else {
				responseModel.setMsg("Error While Creating Activity Claim.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
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
	public Map<String, Object> fetchActivityClaimNoByActivityClaimId(BigInteger activityClaimHdrId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select activity_claim_number from SV_ACTIVITY_CLAIM_HDR (nolock) saah where saah.activity_claim_hdr_id =:activityClaimHdrId";
		mapData.put("ERROR", "Activity CLAIM Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityClaimHdrId", activityClaimHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String activityClaimNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					activityClaimNo = (String) row.get("activity_claim_number");
				}
				mapData.put("activityClaimNo", activityClaimNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY CLAIM DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY CLAIM DETAILS");
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
	public ActivityClaimSearchMainResponseModel fetchActivityClaim(String userCode,
			ActivityClaimSearchRequestModel acRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"fetchActivityClaim invoked.." + userCode + "::::::::::acRequestModel::::::::::" + acRequestModel);
		}
		NativeQuery<?> query = null;
		Session session = null;
		ActivityClaimSearchListResponseModel responseModel = null;
		List<ActivityClaimSearchListResponseModel> responseModelList = null;
		ActivityClaimSearchMainResponseModel acMainSearch = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SV_SAC_SEARCH_ACTIVITYCLAIMS] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :activityClaimNumber, :fromDate, :toDate , :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", acRequestModel.getPcId());
			query.setParameter("orgHierId", acRequestModel.getOrgHierId());
			query.setParameter("dealerId", acRequestModel.getDealerId());
			query.setParameter("branchId", acRequestModel.getBranchId());
			query.setParameter("activityClaimNumber", acRequestModel.getActivityClaimNumber());
			query.setParameter("fromDate", acRequestModel.getFromDate());
			query.setParameter("toDate", acRequestModel.getToDate());
			query.setParameter("includeInactive", 'N');
			query.setParameter("page", acRequestModel.getPage());
			query.setParameter("size", acRequestModel.getSize());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityClaimSearchListResponseModel>();
				acMainSearch = new ActivityClaimSearchMainResponseModel();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					responseModel = new ActivityClaimSearchListResponseModel();
					responseModel.setDealerId((BigInteger) row.get("dealer_id"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setActivityPlanNo((String) row.get("activity_plan_no"));
					responseModel.setActivityClaimStatus((String) row.get("activity_claim_status"));
					responseModel.setActivityClaimNo((String) row.get("activity_claim_no"));
					responseModel.setClaimApprovedAmount((BigDecimal) row.get("claim_approved_amount"));
					responseModel.setActivityFromDate((String) row.get("activity_from_date"));
					responseModel.setActivityToDate((String) row.get("activity_to_date"));
					responseModel.setActivityClaimHdrId((BigInteger) row.get("activity_claim_hdr_id"));
					responseModel.setActivityPlanDate((String) row.get("activity_plan_date"));
					responseModel.setDealerCode((String) row.get("dealer_code"));
					responseModel.setDealerName((String) row.get("dealer_name"));
					responseModel.setProfitCenter((String) row.get("profit_center"));
					responseModel.setTotalBudget((BigDecimal) row.get("total_budget"));
					responseModel.setTotalApprovedBudget((BigDecimal) row.get("total_approved_budget"));
					responseModel.setTotalReimbursement((BigDecimal) row.get("total_reimbursement"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
				}
				acMainSearch.setAcSearch(responseModelList);
				acMainSearch.setRecordCount(recordCount);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return acMainSearch;
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
		String sqlQuery = "EXEC [SV_ACTIVITY_CLIAM_VIEW] :ClaimId,:FLAG ";
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
		String sqlQuery = "EXEC [SV_ACTIVITY_CLIAM_VIEW] :ClaimId,:FLAG ";
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkActivityPermissions invoked..");
		}
		Session session = null;
		Query query = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		String sqlQuery = "exec [SV_SACT_VALIDATE_APPROVAL_PERMISSIONS] :userCode, :id, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("id", requestModel.getId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("IsApprovePermssion", (String) row.get("IsApprovePermssion"));
					mapData.put("IsRejectPermssion", (String) row.get("IsRejectPermssion"));
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
		return mapData;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActivityClaimApproveResponse approveAndRejectActivityClaim(String userCode,
			ActivityClaimApproveRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("approveAndRejectActivityClaim invoked.." + requestModel);
		}
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		Map<String, Object> mapData = null;
		ActivityClaimApproveResponse responseModel = new ActivityClaimApproveResponse();
		boolean isSuccess = true;
		String sqlQuery = "exec SV_SACT_ACTIVITYCLAIM_APPROVAL :userCode, :hoUserId, :activityClaimHdrId, :approvalStatus, :remark, :approvedClaimAmount";
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
					query.setParameter("activityClaimHdrId", requestModel.getActivityClaimHdrId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remark", requestModel.getRemarks());
					query.setParameter("approvedClaimAmount", requestModel.getApprovedClaimAmount());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List<?> data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map<?, ?> row = (Map<?, ?>) object;
							msg = (String) row.get("msg");
							responseModel.setMsg(msg);
							approvalStatus = (String) row.get("approvalStatus");
							responseModel.setApprovalStatus(approvalStatus);
						}
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Error While Validating Approval.");
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

//	@Override
//	public String fetchActivityClaimByPlanId(BigInteger activityPlanHdrId) {
//	
//	 String result = null;
//
//	    if (logger.isDebugEnabled()) {
//	        logger.debug("fetchActivityClaimByPlanId invoked.." + activityPlanHdrId);
//	    }
//	    
//	    Query query = null;
//	    Session session = null;
//	    
//	    String sqlQuery = "select ac.activity_claim_number from SV_ACTIVITY_CLAIM_HDR ac WHERE ac.activity_plan_hdr_id = :activityPlanId";
//
//	    
//	    try {
//	        session = sessionFactory.openSession();
//	        query = session.createSQLQuery(sqlQuery);
//	        query.setParameter("activityPlanId", activityPlanHdrId);
//	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//	        // Assuming that the stored procedure returns a single result
//	        Object singleResult = query.uniqueResult();
//	        
//	        // Convert the result to String
//	        if (singleResult != null) {
//	            result = singleResult.toString();
//	        }
//	    } catch (SQLGrammarException exp) {
//	        logger.error(this.getClass().getName(), exp);
//	    } catch (HibernateException exp) {
//	        logger.error(this.getClass().getName(), exp);
//	    } catch (Exception exp) {
//	        logger.error(this.getClass().getName(), exp);
//	    } finally {
//	        if (session != null) {
//	            session.close();
//	        }
//	    }
//	    
//	    return result;
//	}
	@Override
	public Map<String, Object> fetchActivityClaimByPlanId(BigInteger activityPlanId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimByPlanId invoked.." + activityPlanId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "select ac.activity_claim_number from SV_ACTIVITY_CLAIM_HDR ac WHERE ac.activity_plan_hdr_id = :activityPlanId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("activityPlanId", activityPlanId);
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
	public List<ActivityResponseModel> fetchPlanNumberForClaim(String userCode,
			ActivityRequestModel activityPlanNoAutoRequestModel) {
		Session session = null;
		ActivityResponseModel activityResponseModel = null;
		List<ActivityResponseModel> activityPlanNoList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SV_GET_ACTIVITY_PLANNUMBER_FOR_CLAIM] :userCode";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			activityPlanNoList = new ArrayList<ActivityResponseModel>();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityResponseModel = new ActivityResponseModel();
					activityResponseModel.setActivityPlanId((Integer) row.get("ActivityPlanId"));
					activityResponseModel.setActivityPlanNumber((String) row.get("ActivityPlan"));
					activityResponseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					activityPlanNoList.add(activityResponseModel);
				}
			}

		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return activityPlanNoList;
	}

	@Override
	public void approveAndRejectActivityClaimDetail(String userCode, List<ActivityClaim> activityList) {

		org.hibernate.query.Query<?> query = null;
		String sqlQuery = "exec SV_SACT_ACTIVITYCLAIM_APPROVAL_DETAIL :userCode, :activityClaimHdrId, :approvalBudget";
		try {
					for(ActivityClaim bean:activityList) {
						Session session = sessionFactory.getCurrentSession();
						query = session.createSQLQuery(sqlQuery);
						
						query.setParameter("userCode", userCode);
						query.setParameter("activityClaimHdrId", bean.getActivityId());
						query.setParameter("approvalBudget", bean.getApprovalBudget());
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						query.executeUpdate();
					}
		 
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
	 }
	

}
