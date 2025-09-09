package com.hitech.dms.web.dao.activityClaim.approval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.model.activityClaim.approval.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimDtlListResponseModel;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimHdrDtlViewResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class ActivityClaimApprovalDaoImpl implements ActivityClaimApprovalDao {

	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimApprovalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ActivityCommonDao activityCommonDao;

	@Override
	public ActivityClaimHdrDtlViewResponseModel fetchActivityHdrAndDtlView(String userCode, BigInteger dealerId,
			BigInteger id, String isFor,  Device device) {
		ActivityClaimHdrDtlViewResponseModel responselList = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			responselList = new ActivityClaimHdrDtlViewResponseModel();
			ActivityClaimHdrResponseModel hh = activityCommonDao.fetchActivityClaimHdr(session, userCode, id,isFor);

			if (hh != null) {
				responselList.setActHdr(hh);
				List<ActivityClaimDtlListResponseModel> nn=  activityCommonDao.fetchActivityClaimDtlList(session, userCode, dealerId, id,isFor);
				if (nn != null) {
					responselList.setActDtl(nn);
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

	@SuppressWarnings("deprecation")
	@Override
	public ActivityClaimApproveResponse approveRejectActivityClaim(String userCode, ActivityClaimApproveRequestModel acaRequestModel, Device device) {
		
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		Map<String, Object> mapData = null;
		String approvalDesigLevel=null;
		ActivityClaimApproveResponse responseModel = new ActivityClaimApproveResponse();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SACT_ACTIVITYCLAIM_APPROVAL] :userCode, :hoUserId, :activityClaimHdrId, :approvalStatus, :remark";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			
			mapData = activityCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = activityCommonDao.fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("activityClaimHdrId", acaRequestModel.getActivityClaimHdrId());
					query.setParameter("approvalStatus", acaRequestModel.getApprovalStatus());
					query.setParameter("remark", acaRequestModel.getRemarks());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List<?> data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map<?, ?> row = (Map<?, ?>) object;
							msg = (String) row.get("msg");
							responseModel.setMsg(msg);
							approvalStatus = (String) row.get("approvalStatus");
							approvalDesigLevel=(String) row.get("desigLevelDesc");
							
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
				Integer updateDetailStatus=updateCLaimRevisedAmount(userCode,acaRequestModel.getActivityClaimHdrId(),acaRequestModel.getRevisedApprovedAmt(),acaRequestModel.getActClaimDtlList(),acaRequestModel.getApprovalLevelSequence(),session);
				System.out.println("the updateDetaiLStatus is "+updateDetailStatus);
				if(updateDetailStatus!=null&& !acaRequestModel.getRevisedApprovedAmt().isEmpty())
				{
					
						if(updateDetailStatus>=1) {
							
							transaction.commit();
							responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
							responseModel.setApprovalStatus(approvalStatus);
							responseModel.setMsg(msg);
						}
						else
						{
							transaction.rollback();
							responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
							responseModel.setApprovalStatus(approvalStatus);
							responseModel.setMsg("Revised Amount Not updated Successfully");
						}
						
					
					
				}
				
				else
				{
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("Revised Approved  Amount Is Empty ");
				}
				
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
			
			if(acaRequestModel.getApprovalStatus().equalsIgnoreCase("Approve")) {
				if(approvalDesigLevel !=null && approvalDesigLevel.equalsIgnoreCase("State Head - Sales")) {
					try {
						updateActivityClaimMail(userCode, "Plan Claim Approval","state", acaRequestModel.getActivityClaimHdrId()).subscribe(e -> {
							logger.info(e.toString());
						});
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
				}else if(approvalDesigLevel !=null && approvalDesigLevel.equalsIgnoreCase("Marketing Dev. head")) {
					try {
						updateActivityClaimMail(userCode, "Plan Claim Approval","market", acaRequestModel.getActivityClaimHdrId()).subscribe(e -> {
							logger.info(e.toString());
						});
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
				}else if(approvalDesigLevel !=null && approvalDesigLevel.equalsIgnoreCase("Finance Head")) {
					try {
						updateActivityClaimMail(userCode, "Plan Claim Approval","finance", acaRequestModel.getActivityClaimHdrId()).subscribe(e -> {
							logger.info(e.toString());
						});
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
				}
			}else {
				try {
					updateActivityClaimMail(userCode, "Plan Claim Approval","Rejected", acaRequestModel.getActivityClaimHdrId()).subscribe(e -> {
						logger.info(e.toString());
					});
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				}
			}
			
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	private Integer updateCLaimRevisedAmount(String userCode, BigInteger activityClaimHdrId,
			List<BigDecimal> revisedApprovedAmt,List<BigInteger> claimDtlList,Integer approvalLevelSequence,Session session1) {
		 //  Session session=null;
		    logger.info("updateCLaimRevisedAmount invoked "+revisedApprovedAmt.size());
		  Integer  updateCLaimStatus=0;
		try
		{
				NativeQuery<?> query = null;
			for(int i=0;i<revisedApprovedAmt.size();i++) {
				
				String sqlQuery="SA_UPDATE_ACTIVITY_CLAIM_DTL :activity_clim_hdr_id,:revisedAmt,:activity_claim_dtl_id,:userCode,:approvalLevelSeq";
				query = session1.createNativeQuery(sqlQuery);
				BigDecimal approvedAmt=revisedApprovedAmt.get(i);
				query.setParameter("activity_clim_hdr_id",activityClaimHdrId);
				query.setParameter("revisedAmt",approvedAmt);
				query.setParameter("activity_claim_dtl_id",claimDtlList.get(i));
				query.setParameter("userCode",userCode);	
				query.setParameter("approvalLevelSeq",approvalLevelSequence);	
				query.executeUpdate();
				updateCLaimStatus=+1;;
				}
				
			
			
			
			
			
		}catch(Exception e )
		{
		
			e.printStackTrace();
		}
		
		return updateCLaimStatus;
	}

	


	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateActivityClaimMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		if(flag !=null && flag.equalsIgnoreCase("Rejected")) {
			sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_Plan_CLAIM_REJECT] :userCode, :eventName, :refId, :isIncludeActive";
		}else if(flag.equalsIgnoreCase("state")) {
			sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_Plan_CLAIM_APPROVAL_STATE_HEAD] :userCode, :eventName, :refId, :isIncludeActive";
		}
		else if(flag.equalsIgnoreCase("market")) {
			sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_Plan_CLAIM_APPROVAL_Marketing_HEAD] :userCode, :eventName, :refId, :isIncludeActive";
		}else if(flag.equalsIgnoreCase("finance")) {
			sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_Plan_CLAIM_APPROVAL_Finance_HEAD] :userCode, :eventName, :refId, :isIncludeActive";
		}
		
		
		
		mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
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
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY PLAN SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY PLAN SUBMISSION MAIL TRIGGERS.");
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

}
