/**
 * 
 */
package com.hitech.dms.web.dao.dc.cancel;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.delivery.DeliveryChallanCancelApprovalEntity;
import com.hitech.dms.web.model.dc.cancel.request.DcCancelRequestModel;
import com.hitech.dms.web.model.dc.cancel.response.DcCancelResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcCancelDaoImpl implements DcCancelDao {
	private static final Logger logger = LoggerFactory.getLogger(DcCancelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public DcCancelResponseModel cancelDc(String authorizationHeader, String userCode,
			DcCancelRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create cancelDc invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DcCancelResponseModel responseModel = new DcCancelResponseModel();
		boolean isSuccess = false;
		String sqlQuery = "exec [SP_SA_DC_CANCEL_REQUEST] :userCode, :pcId, :dealerId, :branchId, :dcId, :dcNumber, :dcCancelReasonId, :dcCancelDate, :dcCancelRemark, :dcCancelType";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("pcId", requestModel.getPcId());
				query.setParameter("dealerId", requestModel.getDealerId());
				query.setParameter("branchId", requestModel.getBranchId());
				query.setParameter("dcId", requestModel.getDcId());
				query.setParameter("dcNumber", requestModel.getDcNumber());
				query.setParameter("dcCancelReasonId", requestModel.getDcCancelReasonId());
				query.setParameter("dcCancelDate", requestModel.getDcCancelDate());
				query.setParameter("dcCancelRemark", requestModel.getDcCancelRemark());
				query.setParameter("dcCancelType", requestModel.getDcCancelType());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					BigInteger dcCancelRequestId = null;
					for (Object object : data) {
						Map row = (Map) object;
						responseModel.setDcId((BigInteger) row.get("DcId"));
						responseModel.setDcNumber((String) row.get("DcNumber"));
						dcCancelRequestId = (BigInteger) row.get("dcCancelRequestId");
						responseModel.setMsg((String) row.get("msg"));
						responseModel.setStatusCode((Integer) row.get("StatusCode"));
					}
					if (dcCancelRequestId != null && dcCancelRequestId.compareTo(BigInteger.ZERO) > 0) {
//						sqlQuery = "Select * from SA_MACHINE_DC_HDR (nolock) where dc_id =:dcId";
//						query = session.createNativeQuery(sqlQuery).addEntity(DeliveryChallanHdrEntity.class);
//						query.setParameter("dcId", requestModel.getDcId());
//						DeliveryChallanHdrEntity challanHdrEntity = (DeliveryChallanHdrEntity) query.uniqueResult();
//						if (challanHdrEntity != null) {
						mapData = saveIntoApproval(session, userId, null, dcCancelRequestId);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							isSuccess = true;
						} else {
							isSuccess = false;
							responseModel.setMsg("Error While Updating DC Cancel Approval Hier.");
						}
//						}
					} else {
						isSuccess = false;
						logger.info("Cancel Id Not Generated.");
						responseModel.setMsg("Error While Updating DC Cancel Approval Hier.");
					}

					if (isSuccess) {
						transaction.commit();
					}
				}
			} else {
				// User not found
				responseModel.setMsg("User Not Found.");
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} finally {
			
			try {
				dcCancellationMail(userCode, "DC Cancellation","dc cancel", requestModel.getDcId()).subscribe(e -> {
					logger.info(e.toString());
				});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
			
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Cancelling DC.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger dcCancelRequestId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO DC CANCEL APPROVAL TABLE.");
		try {
			List data = commonDao.fetchApprovalData(session, "SA_DC_CANCEL_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					DeliveryChallanCancelApprovalEntity approvalEntity = new DeliveryChallanCancelApprovalEntity();
					approvalEntity.setDcCancelRequestId(dcCancelRequestId);
					approvalEntity.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
					approvalEntity.setApprovalStatus((String) row.get("approvalStatus"));
					approvalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					approvalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						approvalEntity.setIsFinalApprovalStatus('Y');
					} else {
						approvalEntity.setIsFinalApprovalStatus('N');
					}
					approvalEntity.setRejectedFlag('N');
					approvalEntity.setHoUserId(null);

					session.save(approvalEntity);
				}
			}
			mapData.put("SUCCESS", "Inserted Into DC Cancel Approval Table.");
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
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> dcCancellationMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		if(flag !=null && flag.equalsIgnoreCase("dc cancel")) {
			sqlQuery = "exec [SP_MAIL_SA_DC_CANCEL_MAIL] :userCode, :eventName, :refId, :isIncludeActive";
		}
		
		//mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
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
			mapData.put("ERROR", "ERROR WHILE INSERTING DC CANCEL MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING DC CANCEL MAIL TRIGGERS.");
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
