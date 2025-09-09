/**
 * 
 */
package com.hitech.dms.web.dao.activityClaim.inv.create.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.dozer.DozerBeanMapper;
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
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.entity.activityClaim.invoice.ActivityClaimInvoiceApprovalEntity;
import com.hitech.dms.web.entity.activityClaim.invoice.ActivityClaimInvoiceDtlEntity;
import com.hitech.dms.web.entity.activityClaim.invoice.ActivityClaimInvoiceHdrEntity;
import com.hitech.dms.web.model.activityClaim.invoice.create.request.ActivityClaimInvCreateRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.create.response.ActivityClaimInvCreateResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityClaimInvCreateDaoImpl implements ActivityClaimInvCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private ActivityCommonDao activityCommonDao;

	public ActivityClaimInvCreateResponseModel createActivityClaimInvoice(String userCode,
			ActivityClaimInvCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createActivityClaimInvoice invoked..");
		}
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		Map<String, Object> branchDetails = null;
		ActivityClaimInvCreateResponseModel responseModel = new ActivityClaimInvCreateResponseModel();
		ActivityClaimInvoiceHdrEntity activityClaimInvoiceHdrEntity = null;
		boolean isSuccess = true;
		String msg = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = activityCommonDao.fetchUserDTLByUserCode(session, userCode);
			branchDetails=activityCommonDao.fetchBranchDetials(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					Query query = null;
					String sqlQuery = "update SA_ACT_CLAIM_INVOICE_HDR set customer_invoice_no=:custInvNo,customer_invoice_date=:custInvDate,final_submit=:finalSub where activity_claim_inv_hdr_id =:idClaim";
		            try {
		            	query = session.createNativeQuery(sqlQuery);
		            	query.setParameter("custInvNo", requestModel.getCustomerInvoiceNo());
		    			query.setParameter("custInvDate", requestModel.getCustomerInvoiceDate());
		    			query.setParameter("finalSub", requestModel.getFinalSubmitFlag());
		    			query.setParameter("idClaim", requestModel.getActivityClaimHdrId());
		    			
		    			int k = query.executeUpdate();
		            }
		            catch (SQLGrammarException ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		} catch (Exception ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		}
				}else {
					activityClaimInvoiceHdrEntity = mapper.map(requestModel, ActivityClaimInvoiceHdrEntity.class,
							"ActivityClaimCreateResMapId");
					List<ActivityClaimInvoiceDtlEntity> activityClaimInvoiceDtlList = activityClaimInvoiceHdrEntity
							.getActivityClaimInvDtlList();
					if (activityClaimInvoiceDtlList != null && !activityClaimInvoiceDtlList.isEmpty()) {
						for (ActivityClaimInvoiceDtlEntity claimInvoiceDtlEntity : activityClaimInvoiceDtlList) {
							if (claimInvoiceDtlEntity.getDiscountAmount() == null
									|| claimInvoiceDtlEntity.getDiscountAmount().compareTo(BigDecimal.ZERO) == 0) {
								claimInvoiceDtlEntity.setApprovedAmount(claimInvoiceDtlEntity.getApprovedAmount());
							}
							claimInvoiceDtlEntity.setActivityClaimInvoice(activityClaimInvoiceHdrEntity);
						}
						// current date
						Date currDate = new Date();
						
						String docnum = null;
						BigInteger branchId = null;
						branchId = (BigInteger) branchDetails.get("branchId");
						docnum = getDocumentNumber("ACI",branchId.intValue(), session);
			
						if (docnum == null || docnum.equals("")) {
							responseModel.setMsg("Document No. not generatng");
							responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
							return responseModel;
						}
						
						/*
						 * activityClaimInvoiceHdrEntity .setClaimInvoiceNo("ACI/" +
						 * activityClaimInvoiceHdrEntity.getActivityClaimHdrId() + "/" +
						 * activityClaimInvoiceHdrEntity.getDealerId() + "/" + currDate.getTime());
						 */
						
						activityClaimInvoiceHdrEntity.setClaimInvoiceNo(docnum);
						activityClaimInvoiceHdrEntity.setInvoiceType("");
						activityClaimInvoiceHdrEntity.setClaimInvoiceDate(currDate);
						activityClaimInvoiceHdrEntity.setClaimInvoiceStatus(WebConstants.PENDING);
						activityClaimInvoiceHdrEntity.setCreatedBy(userId);
						activityClaimInvoiceHdrEntity.setCreatedDate(new Date());
						session.save(activityClaimInvoiceHdrEntity);
						
						updateDocumentNumber(docnum.substring(docnum.length() - 7), "ACI",branchId + "", session,"Actvity GST Invoice");
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Claim Invoice Detail Not Found.");
					}
				}
				
				
				
				
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					transaction.commit();
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					msg = "Activity Claim Invoice Created Successfully";
					responseModel.setMsg(msg);
				}else {
					mapData = saveIntoApproval(session, userId, null,
							activityClaimInvoiceHdrEntity.getActivityClaimInvHdrId(),
							activityClaimInvoiceHdrEntity.getTotalInvoiceAmnt());
					if (mapData != null && mapData.get("SUCCESS") != null) {
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
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					
				}else {
					updateActivityClaimGstInvoiceMail(userCode, "Activity Claim GST Invoice", activityClaimInvoiceHdrEntity.getActivityClaimInvHdrId()).subscribe(e -> {
						logger.info(e.toString());
					});
				}
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
			
			
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					
				}else {
					mapData = fetchActivityClaimInvNoByActivityClaimInvId(
							activityClaimInvoiceHdrEntity.getActivityClaimInvHdrId());
					if (mapData != null && mapData.get("SUCCESS") != null) {
						responseModel.setActivityClaimInvNumber((String) mapData.get("activityClaimInvNo"));
					}
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;

	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateActivityClaimGstInvoiceMail(String userCode, String eventName, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_CLAIM_GST_INVOICE] :userCode, :eventName, :refId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM GST INVOICE SUBMISSION MAIL TRIGGERS..");
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
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM GST INVOICE SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY CLAIM GST INVOICE SUBMISSION MAIL TRIGGERS..");
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
	public Map<String, Object> fetchActivityClaimInvNoByActivityClaimInvId(BigInteger activityClaimInvId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select claim_invoice_no from SA_ACT_CLAIM_INVOICE_HDR (nolock) sacih where sacih.activity_claim_inv_hdr_id =:activityClaimInvId";
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

	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger activityClaimInvId, BigDecimal approvedAmount) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO ACTIVITY CLAIM INVOICE APPROVAL TABLE.");
		try {
			List data = activityCommonDao.fetchApprovalData(session, "SA_ACTIVITY_CLAIM_INV_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					ActivityClaimInvoiceApprovalEntity activityClaimInvApprovalEntity = new ActivityClaimInvoiceApprovalEntity();
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
}
