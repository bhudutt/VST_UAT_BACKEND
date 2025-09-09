package com.hitech.dms.web.dao.pr.approval;

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
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.model.pr.approval.request.PurchaseReturnApprovalRequestModel;
import com.hitech.dms.web.model.pr.approval.response.PurchaseReturnApprovalResponseModel;

import reactor.core.publisher.Mono;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Repository
public class PurchaseReturnApprovalDaoImpl implements PurchaseReturnApprovalDao {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnApprovalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MachinePOCommonDao machinePOCommonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PurchaseReturnApprovalResponseModel approveRejectPurchaseReturn(String userCode,
			PurchaseReturnApprovalRequestModel requestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PurchaseReturnApprovalResponseModel responseModel = new PurchaseReturnApprovalResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SA_GRN_PR_APPROVAL] :userCode, :hoUserId, :purchaseReturnId, :approvalStatus, :remarks";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = machinePOCommonDao.fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("purchaseReturnId", requestModel.getPurchaseReturnId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remarks", requestModel.getRemarks());
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
						responseModel.setMsg("Error While Validating Machine Purchase Return Approval.");
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
				transaction.commit();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setApprovalStatus(approvalStatus);
				responseModel.setMsg(msg);
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
			if (isSuccess) {

				try {
					
					updatePurchaseReturnMail(userCode, WebConstants.PURCHASE_RETURN,
							requestModel.getPurchaseReturnId()).subscribe(e -> {
								logger.info(e.toString());
							});
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				}
				
				
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updatePurchaseReturnMail(String userCode, String eventName, BigInteger enqHDRId) {
	Session session = null;
	Map<String, Object> mapData = new HashMap<String, Object>();
	Query query = null;
	BigInteger mailItemId = null;
	String sqlQuery = "exec [SP_MAIL_SA_PUR_RET_MAIL] :usercode , :event_name, :enqHDRId, :includeInactive";
	mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS..");
	try {
		session = sessionFactory.openSession();
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("usercode ", userCode);
		query.setParameter("event_name", eventName);
		query.setParameter("refId", enqHDRId);
		query.setParameter("includeInactive", "N");
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
		mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS.");
		logger.error(this.getClass().getName(), ex);
	} catch (Exception ex) {
		mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS.");
		logger.error(this.getClass().getName(), ex);
	} finally {
		if (session != null) {
			session.close();
		}
		if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
			PublishModel publishModel = new PublishModel();
			publishModel.setId(mailItemId);
			//publishModel.setTopic(senderfollowupEnqTopicExchange.getName());
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				// Simulate a long-running Job
				try {
	//				rabbitTemplate.convertAndSend(senderfollowupEnqTopicExchange.getName(), routingKey,
	//						commonUtils.objToJson(publishModel).toString());
	//				logger.info("Published message for followup enquiry '{}'", publishModel.toString());
	
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
