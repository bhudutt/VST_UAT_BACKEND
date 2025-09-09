/**
 * 
 */
package com.hitech.dms.web.dao.inv.cancel.approval;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.inv.cancel.approval.request.InvCancelApprovalRequestModel;
import com.hitech.dms.web.model.inv.cancel.approval.response.InvCancelApprovalResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvCancelApprovalDaoImpl implements InvCancelApprovalDao {
	private static final Logger logger = LoggerFactory.getLogger(InvCancelApprovalDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public InvCancelApprovalResponseModel approveRejectInvCancel(String userCode,
			InvCancelApprovalRequestModel requestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		InvCancelApprovalResponseModel responseModel = new InvCancelApprovalResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SA_INV_CANCEL_APPROVAL] :userCode, :hoUserId, :invId, :InvCancelRequestId, :approvalStatus, :remark";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("invId", requestModel.getSalesInvoiceHdrId());
					query.setParameter("InvCancelRequestId", requestModel.getInvCancelRequestId());
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
						responseModel.setMsg("Error While Validating DC Cancel Approval.");
					}
				} else {
					isSuccess = false;
					responseModel.setMsg("HO User Not Found.");
				}
			} else {
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				if(responseModel.getMsg() != null && responseModel.getMsg().contains("Successful")) {
					transaction.commit();
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					responseModel.setApprovalStatus(approvalStatus);
					responseModel.setMsg(msg);
				}
			}else {
				isSuccess = false;
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
			try {
				InvoiceCancellationApprovalMail(userCode, "Invoice Cancellation Approved",requestModel.getApprovalStatus(), requestModel.getSalesInvoiceHdrId()).subscribe(e -> {
					logger.info(e.toString());
				});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
			
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> InvoiceCancellationApprovalMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		//if(flag !=null && flag.equalsIgnoreCase("dc cancel")) {
			sqlQuery = "exec [SP_MAIL_SA_INVOICE_APPROVAL_MAIL] :userCode, :eventName, :refId, :isIncludeActive, :status";
		//}
		
		//mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("refId", hDRId);
			query.setParameter("isIncludeActive", "N");
			query.setParameter("status", flag);
			
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
			mapData.put("ERROR", "ERROR WHILE INSERTING INVOICE CANCEL APPROVAL MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING INVOICE CANCEL APPROVAL MAIL TRIGGERS.");
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
