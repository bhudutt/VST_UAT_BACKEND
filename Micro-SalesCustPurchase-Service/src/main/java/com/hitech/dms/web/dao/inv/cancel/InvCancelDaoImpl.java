/**
 * 
 */
package com.hitech.dms.web.dao.inv.cancel;

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
import com.hitech.dms.web.entity.invoice.MachineInvoiceCancelAppEntity;
import com.hitech.dms.web.model.inv.cancel.request.InvCancelRequestModel;
import com.hitech.dms.web.model.inv.cancel.response.InvCancelResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvCancelDaoImpl implements InvCancelDao {
	private static final Logger logger = LoggerFactory.getLogger(InvCancelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public InvCancelResponseModel cancelInvoice(String authorizationHeader, String userCode,
			InvCancelRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create cancelInvoice invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		InvCancelResponseModel responseModel = new InvCancelResponseModel();
		boolean isSuccess = false;
		String sqlQuery = "exec [SP_SA_INV_CANCEL_REQUEST] :userCode, :pcId, :dealerId, :branchId, :invId, :invNumber, :InvCancelReasonId, :InvCancelDate, :InvCancelRemark, :InvCancelType";
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
				query.setParameter("invId", requestModel.getSalesInvoiceHdrId());
				query.setParameter("invNumber", requestModel.getInvoiceNumber());
				query.setParameter("InvCancelReasonId", requestModel.getInvCancelReasonId());
				query.setParameter("InvCancelDate", requestModel.getInvCancelDate());
				query.setParameter("InvCancelRemark", requestModel.getInvCancelRemark());
				query.setParameter("InvCancelType", requestModel.getInvCancelType());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					BigInteger invCancelRequestId = null;
					for (Object object : data) {
						Map row = (Map) object;
						responseModel.setSalesInvoiceHdrId((BigInteger) row.get("SalesInvoiceHdrId"));
						responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
						invCancelRequestId = (BigInteger) row.get("InvCancelRequestId");
						responseModel.setMsg((String) row.get("msg"));
						responseModel.setStatusCode((Integer) row.get("StatusCode"));
					}
					if (invCancelRequestId != null && invCancelRequestId.compareTo(BigInteger.ZERO) > 0) {
						mapData = saveIntoApproval(session, userId, null, invCancelRequestId);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							isSuccess = true;
						} else {
							isSuccess = false;
							responseModel.setMsg("Error While Updating Invoice Cancel Approval Hier.");
						}
					} else {
						isSuccess = false;
						logger.info("Invoice Cancel Id Not Generated.");
						responseModel.setMsg("Error While Updating Machine Invoice Cancel Approval Hier.");
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
				invCancellationMail(userCode, "INVOICE Cancellation","inv cancel", requestModel.getSalesInvoiceHdrId()).subscribe(e -> {
					logger.info(e.toString());
				});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Cancelling Machine Invoice.");
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
			BigInteger InvCancelRequestId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO INVOICE CANCEL APPROVAL TABLE.");
		try {
			List data = commonDao.fetchApprovalData(session, "SA_INV_CANCEL_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					MachineInvoiceCancelAppEntity approvalEntity = new MachineInvoiceCancelAppEntity();
					approvalEntity.setInvCancelRequestId(InvCancelRequestId);
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
			mapData.put("SUCCESS", "Inserted Into Invoice Cancel Approval Table.");
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
	private Mono<Map<String, Object>> invCancellationMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		if(flag !=null && flag.equalsIgnoreCase("inv cancel")) {
			sqlQuery = "exec [SP_MAIL_SA_INVOICE_CANCEL_MAIL] :userCode, :eventName, :refId, :isIncludeActive";
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
			mapData.put("ERROR", "ERROR WHILE INSERTING INV CANCEL MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING INV CANCEL MAIL TRIGGERS.");
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
