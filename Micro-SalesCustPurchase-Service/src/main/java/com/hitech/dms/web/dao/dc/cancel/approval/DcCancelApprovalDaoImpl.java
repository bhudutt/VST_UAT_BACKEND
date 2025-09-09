/**
 * 
 */
package com.hitech.dms.web.dao.dc.cancel.approval;

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
import com.hitech.dms.web.model.dc.cancel.approval.request.DcCancelApprovalRequestModel;
import com.hitech.dms.web.model.dc.cancel.approval.response.DcCancelApprovalResponseModel;
import com.hitech.dms.web.model.dc.create.request.AllotMachDtlForDCCreateRequestModel;
import com.hitech.dms.web.model.dc.create.request.DcCreateRequestModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcCancelApprovalDaoImpl implements DcCancelApprovalDao {
	private static final Logger logger = LoggerFactory.getLogger(DcCancelApprovalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public DcCancelApprovalResponseModel approveRejectDcCancel(String userCode,
			DcCancelApprovalRequestModel requestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DcCancelApprovalResponseModel responseModel = new DcCancelApprovalResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SA_DC_CANCEL_APPROVAL] :userCode, :hoUserId, :dcId, :dcCancelRequestId, :approvalStatus, :remark";
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
					query.setParameter("dcId", requestModel.getDcId());
					query.setParameter("dcCancelRequestId", requestModel.getDcCancelRequestId());
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
				transaction.commit();
				// update SA_vin_master on successfull cancel approval 
				Integer status=updateVinMasterOnDcCancel(userCode,requestModel.getDcId());
				if(status==1)
				{
					msg=msg+"and update Vin Master successfully";
				}
				else
				{
					msg=msg+"  Vin Master not updated !!";
				}
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
			try {
				dcCancellationApprovalMail(userCode, "DC Cancellation Approved",requestModel.getApprovalStatus(), requestModel.getDcId()).subscribe(e -> {
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
	private Mono<Map<String, Object>> dcCancellationApprovalMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		
			sqlQuery = "exec [SP_MAIL_SA_DC_CANCEL_MAIL] :userCode, :eventName, :refId, :isIncludeActive,:status";	
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
			mapData.put("ERROR", "ERROR WHILE INSERTING DC CANCEL APPROVAL MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING DC CANCEL APPROVAL MAIL TRIGGERS.");
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
	
	private Integer updateVinMasterOnDcCancel(String userCode, 
			BigInteger dcId) {
		 boolean isSuccess=false;
		 Integer   updateCount =0;
		Integer updateStatus=0;
		Session session= null;
		Query query=null;
		try
		{
			session=sessionFactory.openSession();
			
			if(dcId!=null) {
				
				
					
					System.out.println("in for loop "+ userCode+"  "+dcId);
					 session.beginTransaction();

			            // Create a native SQL query
			            String sqlQuery = "exec SA_UPDATE_VIN_MASTER_DC_CANCEL :dc_id";
			             query = session.createSQLQuery(sqlQuery)
			                    .setParameter("dc_id",dcId);
			                    

			            // Execute the query
			             updateCount = query.executeUpdate();
			            System.out.println("updateCount "+updateCount);
			            if(updateCount==1)
			            {
			            	isSuccess=true;
			            }

			            // Commit the transaction
			            session.getTransaction().commit();


			           

				}
			
			else
			{
				
				updateCount=-1;
				isSuccess=false;
			}
			
			
			
			
		}catch (SQLGrammarException ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} finally {
			
				
			
	}
		
        System.out.println("before send response "+updateCount);

		  return updateCount;

		
		
		
	}
}
