package com.hitech.dms.web.dao.activityClaim.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

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

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.FileUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.entity.activityClaim.ActivityClaimApprovalEntity;
import com.hitech.dms.web.entity.activityClaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimDtlListResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimApproveResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrDtlResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimPlanResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityPlanAutoResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class ActivityClaimCreateDaoImpl implements ActivityClaimCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ActivityCommonDao activityCommonDao;

	@SuppressWarnings("deprecation")
	@Override
	public List<ActivityPlanAutoResponseModel> activityPlanAuto(String userCode, BigInteger dealerId, String isFor,
			String searchText) {
		Session session = null;
		ActivityPlanAutoResponseModel activityPlanNo = null;
		List<ActivityPlanAutoResponseModel> activityPlanNoList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SACT_ACTIVITYPLANNO_LIST] :userCode, :dealerId, :isFor, :searchText";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);
			query.setParameter("isFor", isFor);
			query.setParameter("searchText", searchText);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				activityPlanNoList = new ArrayList<ActivityPlanAutoResponseModel>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityPlanNo = new ActivityPlanAutoResponseModel();
					activityPlanNo.setActivityPlanHdrId((BigInteger) row.get("ActivityPlanHdrId"));
					activityPlanNo.setActivityPlanNo((String) row.get("ActivityNumber"));
					activityPlanNo.setActivityCreationDate((String) row.get("activity_creation_date"));
					activityPlanNoList.add(activityPlanNo);
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
		return activityPlanNoList;
	}

	@Override
	public ActivityClaimHdrDtlResponseModel fetchActivityHdrAndDtl(String userCode, BigInteger dealerId,
			BigInteger activityPlanHdrId, String isFor, Device device) {
		ActivityClaimHdrDtlResponseModel responselList = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			responselList = new ActivityClaimHdrDtlResponseModel();

			ActivityClaimHdrResponseModel actPlanHdr = activityCommonDao.fetchActivityPlanHdr(session, userCode,
					activityPlanHdrId, isFor);

			if (actPlanHdr != null) {
				responselList.setActHdr(actPlanHdr);

				List<ActivityClaimPlanResponseModel> actPlanDtl = fetchActivityPlanDtl(session, userCode, dealerId,
						activityPlanHdrId, isFor);

				if (actPlanDtl != null) {
					responselList.setActDtl(actPlanDtl);
				}
			}

		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responselList;
	}

	public List<ActivityClaimPlanResponseModel> fetchActivityPlanDtl(Session session, String userCode,
			BigInteger dealerId, BigInteger activityPlanHdrId, String isFor) {
		ActivityClaimPlanResponseModel apDtl = null;
		List<ActivityClaimPlanResponseModel> apDtlList = null;
		try {

			List<ActivityClaimDtlListResponseModel> activityClaimDtlList = activityCommonDao
					.fetchActivityClaimDtlList(session, userCode, dealerId, activityPlanHdrId, isFor);
			if (activityClaimDtlList != null && !activityClaimDtlList.isEmpty()) {
				apDtlList = new ArrayList<ActivityClaimPlanResponseModel>();
				for (ActivityClaimDtlListResponseModel responseModel : activityClaimDtlList) {
					apDtl = mapper.map(responseModel, ActivityClaimPlanResponseModel.class,
							"ActivityNoListResponseMapId");
					apDtlList.add(apDtl);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return apDtlList;
	}

	@Override
	public ActivityClaimCreateResponseModel createActivityClaim(String userCode, ActivityClaimHdrEntity activityClaim,
			List<MultipartFile> files, Device device) {

		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		BigInteger activityClaimHdrId = null;
		Date currDate = new Date();
		boolean fileFlag = false;

		ActivityClaimCreateResponseModel responseModel = new ActivityClaimCreateResponseModel();
		try {
			session = sessionFactory.openSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			transaction = session.beginTransaction();
			// activityClaim.setActivityClaimNumber("ACT");
			activityClaim.setActivityClaimNumber("ACT/" + activityClaim.getActivityPlanHdrId() + "/"
					+ activityClaim.getDealerId() + "/" + currDate.getTime());
			activityClaim.setActivityClaimStatus(WebConstants.PENDING);
			activityClaim.setCreatedBy(userId);
			activityClaim.setCreatedDate(currDate);
			activityClaim.getActivityClaimDtl().forEach(dtl -> {
				dtl.setAcHdr(activityClaim);
				// dtl.setActivityActualHdrId(activityClaim.getActivityActualHdrId());
				if (dtl.getActiityPhotoFile() != null) {
					String[] photo = dtl.getActiityPhotoFile().split(Pattern.quote("\\"));
					dtl.setActiityPhotoFile(photo[photo.length - 1]);
					dtl.setActiityPhotoFile(dtl.getActiityPhotoFile().replaceAll("[^.,a-zA-Z0-9]"," ").replaceAll("\\s","_"));
				}
				if (dtl.getVendorInvoiceFile() != null) {
					String[] invoice = dtl.getVendorInvoiceFile().split(Pattern.quote("\\"));
					// String invoice =
					// dtl.getVendorInvoiceFile().substring(dtl.getVendorInvoiceFile().lastIndexOf('\\'));
					dtl.setVendorInvoiceFile(invoice[invoice.length - 1]);
					dtl.setVendorInvoiceFile(dtl.getVendorInvoiceFile().replaceAll("[^.,a-zA-Z0-9]"," ").replaceAll("\\s","_"));				}

			});
			ActivityClaimApprovalEntity aprrove = null;
			List<ActivityClaimApprovalEntity> aprroveList = null;

			List<?> fetchApprovalData = activityCommonDao.fetchApprovalData(session, "SA_ACTIVITY_CLAIM_APPROVAL");
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
			final BigInteger aclaimHdrId = (BigInteger) session.save(activityClaim);
			activityClaimHdrId = aclaimHdrId;
			// responseModel.setActivityClaimNumber(activityClaimHdrId);
			responseModel.setActivityClaimNumber(activityClaim.getActivityClaimNumber());
			if (activityClaimHdrId != null) {
				if (files != null && !files.isEmpty()) {
					try {
						files.forEach(m -> {
							String filePath = fileStorageProperties.getUploadDirMain()
									+ messageSource.getMessage("activityClaim.file.upload.dir",
											new Object[] { aclaimHdrId.toString() }, LocaleContextHolder.getLocale());
							Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m);
							System.out.println(fileMapData);
							//FileUtils.uploadDOCDTLDOCS(filePath, m);
						});
					} catch (Exception e) {
						logger.error(this.getClass().getName(), e);
						fileFlag = true;
					}

				}
			}
			if (!fileFlag) {
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
			
			try {
				updatePlanClaimMail(userCode, "Plan Claim", activityClaim.getActivityPlanHdrId()).subscribe(e -> {
					logger.info(e.toString());
				});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
			
			
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
			} else {
				responseModel.setMsg("Error While Creating Activity Claim.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updatePlanClaimMail(String userCode, String eventName, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null; 
		String sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_PLAN_CLAIM] :userCode, :eventName, :refId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM SUBMISSION MAIL TRIGGERS.");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("refId", hDRId);
			query.setParameter("isIncludeActive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					mailItemId = (BigInteger) row.get("mailItemId");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderValidatedEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderValidatedEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for validated enquiry '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return Mono.just(mapData);
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
		String sqlQuery = "Select activity_claim_number from SA_ACT_CLAIM_HDR (nolock) saah where saah.activity_claim_hdr_id =:activityClaimHdrId";
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

}
