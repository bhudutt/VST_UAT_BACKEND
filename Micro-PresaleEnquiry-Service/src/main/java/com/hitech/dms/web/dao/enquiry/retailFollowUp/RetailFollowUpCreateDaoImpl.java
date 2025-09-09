package com.hitech.dms.web.dao.enquiry.retailFollowUp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
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

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.enquiry.RetailFollowUpHistory;
import com.hitech.dms.web.model.retailFollowUp.create.request.RetailFollowUpSubmitRequestModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpCashHistoryResponse;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpCreateResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpEnquiryDtlResponse;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpEnquirySelectionResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpHistoryResponse;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowedStageResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailNextStageResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailPaymentReceiptHistory;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailStageModelResponse;

import reactor.core.publisher.Mono;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class RetailFollowUpCreateDaoImpl implements RetailFollowUpCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(RetailFollowUpCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public RetailFollowUpCreateResponseModel createRetailFollowUp(String userCode,
			RetailFollowUpSubmitRequestModel rfuModel, Device device) {
		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		Date currDate = new Date();
		BigInteger retailFollowUpDtlId = null;
		BigInteger userId = null;
		RetailFollowUpCreateResponseModel response = null;
		RetailStageModelResponse lastStageRequest = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [Get_RetailFollow_INSERT] :userCode, :enquiryId, :cashOrLoan,:financierPartyId, :financierStatus, :loanStatus,:disbursementDate, :isLatest, :currentFollowupDate,:followupById, :retailStageId, :remarks,:reasonForRejection, :createdBy,:createdDate,:retailFollowUpHdrId,:expectedRetailDate,:disbursementAmount, :isActive";
		try {
			response = new RetailFollowUpCreateResponseModel();
			List<RetailStageModelResponse> finalList = new ArrayList<>();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			List<RetailStageModelResponse> listR1 = rfuModel.getListR1().stream().collect(Collectors.toList());
//			List<RetailStageModelResponse> listR2 = rfuModel.getListR2().stream().filter(s->s.isChecked()==true).collect(Collectors.toList());
//			List<RetailStageModelResponse> listR3 = rfuModel.getListR3().stream().filter(s->s.isChecked()==true).collect(Collectors.toList());
			finalList.addAll(listR1);
//			finalList.addAll(listR2);
//			finalList.addAll(listR3);
			for(RetailStageModelResponse list:finalList) {
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("enquiryId", rfuModel.getEnquiryId());
				query.setParameter("cashOrLoan", rfuModel.getCashOrLoan());
				query.setParameter("financierPartyId", rfuModel.getFinancierPartyId());
				query.setParameter("financierStatus", rfuModel.getFinancierStatus());
				query.setParameter("loanStatus", rfuModel.getLoanStatus());
				query.setParameter("disbursementDate", rfuModel.getDisbursementDate());
				query.setParameter("isLatest", 'Y');
				query.setParameter("currentFollowupDate", rfuModel.getCurrentFollowupDate());
				query.setParameter("followupById", rfuModel.getFollowupById());
				query.setParameter("retailStageId", list.getRetailStageId());
				query.setParameter("remarks", rfuModel.getRemarks());
				query.setParameter("reasonForRejection", rfuModel.getReasonForRejection());
				query.setParameter("createdBy", userId);
				query.setParameter("createdDate", currDate);
				query.setParameter("retailFollowUpHdrId", rfuModel.getRetailFollowUpHdrId());
				query.setParameter("expectedRetailDate", rfuModel.getExpectedRetailDate());
				query.setParameter("disbursementAmount", rfuModel.getDisbursementAmount());
				query.setParameter("isActive", list.isChecked()?1:0);
				if(list.isChecked()) {
					lastStageRequest = list;
				}
				query.executeUpdate();
			   }
			 }
			
			if(finalList.isEmpty()) {
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("enquiryId", rfuModel.getEnquiryId());
					query.setParameter("cashOrLoan", rfuModel.getCashOrLoan());
					query.setParameter("financierPartyId", rfuModel.getFinancierPartyId());
					query.setParameter("financierStatus", rfuModel.getFinancierStatus());
					query.setParameter("loanStatus", rfuModel.getLoanStatus());
					query.setParameter("disbursementDate", rfuModel.getDisbursementDate());
					query.setParameter("isLatest", 'Y');
					query.setParameter("currentFollowupDate", rfuModel.getCurrentFollowupDate());
					query.setParameter("followupById", rfuModel.getFollowupById());
					query.setParameter("retailStageId", null);
					query.setParameter("remarks", rfuModel.getRemarks());
					query.setParameter("reasonForRejection", rfuModel.getReasonForRejection());
					query.setParameter("createdBy", userId);
					query.setParameter("createdDate", currDate);
					query.setParameter("retailFollowUpHdrId", rfuModel.getRetailFollowUpHdrId());
					query.setParameter("expectedRetailDate", rfuModel.getExpectedRetailDate());
					query.setParameter("disbursementAmount", rfuModel.getDisbursementAmount());
					query.setParameter("isActive",null);
					query.executeUpdate();				
				 }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction.isActive()) {
				transaction.rollback();
			}
			flag = true;
			response.setMsg(e.getMessage());
			response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		} finally {
			if (session.isOpen()) {
				transaction.commit();
				session.close();
			}
			try {
				retailFollowUpHistory(lastStageRequest,rfuModel,userId);
				updateEnqMail(userCode, WebConstants.RETAIL_FOLLOWUP, rfuModel.getEnquiryId())
						.subscribe(e -> {
							logger.info(e.toString());
						});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}			
		}
		if (!flag) {
			response.setMsg("Retail FollowUp is created Successful.");
			response.setStatusCode(WebConstants.STATUS_CREATED_201);

		}

		return response;
	}

	private void retailFollowUpHistory(RetailStageModelResponse lastStageRequest,RetailFollowUpSubmitRequestModel rfuModel, BigInteger userId) {
		Transaction transaction = null;
		Session session=null;
		NativeQuery<?> query = null;
        try{
        	session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = " Select Retail_Followup_HDR_ID from SA_ENQ_RETAIL_FOLLOWUP_HDR where enquiry_id='"+rfuModel.getEnquiryId()+"'";
	
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			List<?> data = query.list();
			
			// Validate if a valid Retail_Followup_HDR_ID exists
            if (data.isEmpty()) {
                System.out.println("No matching Retail_Followup_HDR_ID found for enquiry_id: " + rfuModel.getEnquiryId());
                return;
            }
            // Extract HDR ID from query result
            BigInteger retailFollowupHdrId = new BigInteger(data.get(0).toString());

            // Create new history entry
            RetailFollowUpHistory history = new RetailFollowUpHistory();
            history.setRetailFollowupHdrId(retailFollowupHdrId);
            history.setCurrentFollowupDate(rfuModel.getCurrentFollowupDate()); 
            history.setFollowupByUserId(userId);
            history.setRetailStageId(lastStageRequest.getRetailStageId());
            history.setRemarks(rfuModel.getRemarks());
            history.setReasonForRejection(rfuModel.getReasonForRejection());
            history.setCreatedBy(userId);
            history.setCreatedDate(new Date());

            // Save entity in database
            session.save(history);

            // Commit transaction
            transaction.commit();
            System.out.println("Follow-up history saved successfully!");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of an error
            }
            e.printStackTrace();
        }
   
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
			ex.printStackTrace();
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			ex.printStackTrace();
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateEnqMail(String userCode, String eventName, BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_MAIL_ENQ_RETAILFOLLOWUP] :userCode, :eventName, :enqHDRId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("enqHDRId", enqHDRId);
			query.setParameter("isIncludeActive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return Mono.just(mapData);
	}

	@Override
	public RetailFollowUpEnquirySelectionResponseModel fetchEnqDtlRetailHistory(String userCode, BigInteger enquiryId,
			Device device) {
		RetailFollowUpEnquirySelectionResponseModel responselList = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			responselList = new RetailFollowUpEnquirySelectionResponseModel();
			RetailFollowUpEnquiryDtlResponse enqDtl = fetchRetailFollowUpEnqDtl(session, userCode, enquiryId, 1);
	

			if (enqDtl != null) {
				responselList.setEnqDtl(enqDtl);
				List<RetailFollowUpCashHistoryResponse> cashHistory = fetchretailCashHistory(session, userCode, enquiryId,
						3);
				List<RetailFollowUpHistoryResponse> loanHistory = fetchretailHistory(session, userCode, enquiryId, 2);
				List<RetailPaymentReceiptHistory> retailPaymentHistory = fetchRetailPrHistory(session, enquiryId,
						enqDtl.getBranchId(), 2);
				List<RetailNextStageResponseModel> nextStage = fetchRetailNextStageList(session, userCode, enquiryId);
				Map<String, Object> followUpStages = fetchretailsStages(session, userCode, enquiryId);
				if (cashHistory != null) {
					responselList.setCashHistory(cashHistory);
				}
				if (loanHistory != null) {
					responselList.setLoanHistory(loanHistory);
				}
				if (retailPaymentHistory != null) {
					responselList.setRetailPaymentHistory(retailPaymentHistory);
				}
				if (nextStage != null) {
					responselList.setNextStage(nextStage);
				}
				if (followUpStages != null) {
					responselList.setFollowUpStages(followUpStages);
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
	public RetailFollowUpEnquiryDtlResponse fetchRetailFollowUpEnqDtl(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		NativeQuery<?> query = null;
		RetailFollowUpEnquiryDtlResponse enqDtl = null;
		String sqlQuery = "exec [Get_Enq_Details_RetailFollow] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					enqDtl = new RetailFollowUpEnquiryDtlResponse();
					enqDtl.setEnquiryId((BigInteger) row.get("enquiry_id"));
					enqDtl.setBranchId((BigInteger) row.get("branch_id"));
					enqDtl.setBranchName((String) row.get("BranchName"));
					enqDtl.setEnquiryNumber((String) row.get("enquiry_number"));
					enqDtl.setEnquiryDate((String) row.get("enquiry_date"));
					enqDtl.setEnquiryStatus((String) row.get("enquiry_status"));
					enqDtl.setCustomerName((String) row.get("customer_name"));
					enqDtl.setMobileNo((String) row.get("Mobile_No"));
					enqDtl.setProfitCenter((String) row.get("Profit_Center"));
					enqDtl.setModel((String) row.get("model_name"));
					enqDtl.setVariant((String) row.get("variant"));
					enqDtl.setSeries((String) row.get("series_name"));
					enqDtl.setSegment((String) row.get("segment_name"));
					enqDtl.setItemNo((String) row.get("item_no"));
					enqDtl.setItemDesc((String) row.get("item_description"));
					enqDtl.setEnquiryStageId((Integer) row.get("enquiry_stage_id"));
					enqDtl.setFinancierName((String) row.get("FINANCIER_NAME"));
					enqDtl.setCashOrLoan((String) row.get("CashOrLoan"));
					enqDtl.setFinancierPartyId((BigInteger) row.get("financier_party_id"));
					enqDtl.setRetailFinaceStatus((String) row.get("RetailFinaceStatus"));
					enqDtl.setLoanStatus((String) row.get("LoanStatus"));
					enqDtl.setRetailFollowupHdrId((BigInteger) row.get("Retail_Followup_HDR_ID"));
					enqDtl.setSalesmanId((BigInteger) row.get("salesman_id"));
					Date disbuDate = (Date) row.get("disbursement_date");
					if(disbuDate!=null) {
					enqDtl.setDisbursementDate((Date) row.get("disbursement_date"));
					}
					enqDtl.setDisbursementAmount((BigDecimal) row.get("disbursementAmount"));
					
					
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return enqDtl;
	}

	@SuppressWarnings("deprecation")
	public List<RetailFollowedStageResponseModel> fetchRetailFollowStage(Session session, String userCode,
			BigInteger enquiryId) {
		NativeQuery<?> query = null;
		RetailFollowedStageResponseModel followedStage = null;
		List<RetailFollowedStageResponseModel> followedStageList = null;
		String sqlQuery = "exec [Get_RetailFollow_Stage] :userCode, :enquiryId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				followedStageList = new ArrayList<RetailFollowedStageResponseModel>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					followedStage = new RetailFollowedStageResponseModel();
					followedStage.setRetailStageId((Integer) row.get("retail_stage_id"));
					followedStage.setRetailStageMainCode((String) row.get("Retail_Stage_Main_Code"));
					followedStage.setRetailStageSubValue((String) row.get("Retail_Stage_Sub_Value"));
					followedStage.setGroupSeqNo((Integer) row.get("GroupSeqNo"));
					followedStage.setRetailFollowupHdrId((BigInteger) row.get("Retail_Followup_HDR_ID"));
					followedStageList.add(followedStage);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return followedStageList;
	}

	@SuppressWarnings("deprecation")
	public List<RetailNextStageResponseModel> fetchRetailNextStageList(Session session, String userCode,
			BigInteger enquiryId) {
		NativeQuery<?> query = null;
		RetailNextStageResponseModel retailStage = null;
		List<RetailNextStageResponseModel> retailStageList = null;
		String sqlQuery = "exec [Get_RetailFollow_NextStage] :userCode, :enquiryId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				retailStageList = new ArrayList<RetailNextStageResponseModel>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					retailStage = new RetailNextStageResponseModel();
					retailStage.setRetailStageId((Integer) row.get("retail_stage_id"));
					retailStage.setRetailStageSubValue((String) row.get("Retail_Stage_Sub_Value"));
					retailStage.setIsConsideredRejected((char) row.get("IsConsideredRejected"));
					retailStageList.add(retailStage);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return retailStageList;
	}

	@SuppressWarnings("deprecation")
	public List<RetailFollowUpCashHistoryResponse> fetchretailCashHistory(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		NativeQuery<?> query = null;
		RetailFollowUpCashHistoryResponse cashHistory = null;
		List<RetailFollowUpCashHistoryResponse> cashHistoryList = null;
		String sqlQuery = "exec [Get_Enq_Details_RetailFollow] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				cashHistoryList = new ArrayList<RetailFollowUpCashHistoryResponse>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					cashHistory = new RetailFollowUpCashHistoryResponse();
					cashHistory.setFollowUpDate((String) row.get("Current_Followup_Date"));
					cashHistory.setFollowedBy((String) row.get("FOLLOWED_BY"));
					cashHistory.setRemarks((String) row.get("remarks"));
					cashHistoryList.add(cashHistory);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return cashHistoryList;
	}

	@SuppressWarnings("deprecation")
	public List<RetailFollowUpHistoryResponse> fetchretailHistory(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		NativeQuery<?> query = null;
		RetailFollowUpHistoryResponse retailHistory = null;
		List<RetailFollowUpHistoryResponse> retailHistoryList = null;
		String sqlQuery = "exec [Get_Enq_Details_RetailFollow] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				retailHistoryList = new ArrayList<RetailFollowUpHistoryResponse>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					retailHistory = new RetailFollowUpHistoryResponse();
					retailHistory.setFollowUpDate((String) row.get("Current_Followup_Date"));
					retailHistory.setFollowedBy((String) row.get("FOLLOWED_BY"));
					retailHistory.setFinancier((String) row.get("PartyName"));
					retailHistory.setRetailFinancierStage((String) row.get("finance_status"));
//					retailHistory.setLoanStatus((String) row.get("loan_status"));
					retailHistory.setReasonForRejection((String) row.get("Reason_for_rejection"));
					retailHistory.setRemarks((String) row.get("remarks"));
					retailHistoryList.add(retailHistory);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return retailHistoryList;
	}

	@SuppressWarnings("deprecation")
	public List<RetailPaymentReceiptHistory> fetchRetailPrHistory(Session session, BigInteger enquiryId,
			BigInteger branchId, int flag) {
		NativeQuery<?> query = null;
		RetailPaymentReceiptHistory paymentHist = null;
		List<RetailPaymentReceiptHistory> paymentHistList = null;
		String sqlQuery = "exec [SP_FM_View_PaymentReceiptDtls] :enquiryId,:branchId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("branchId", branchId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				paymentHistList = new ArrayList<RetailPaymentReceiptHistory>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					paymentHist = new RetailPaymentReceiptHistory();
					paymentHist.setReceiptDate((String) row.get("receipt_date"));
					paymentHist.setReceiptNo((String) row.get("receipt_no"));
					paymentHist.setReceiptType((String) row.get("receipt_type"));
					paymentHist.setReceiptMode((String) row.get("receipt_mode"));
					paymentHist.setReceiptAmount((BigDecimal) row.get("receipt_amount"));
					paymentHist.setReceiptRemarks((String) row.get("remarks"));
					paymentHistList.add(paymentHist);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return paymentHistList;
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> fetchretailsStages(Session session, String userCode, BigInteger enquiryId) {
		Map<String, Object> mapData = new HashMap<>();
		;
		RetailStageModelResponse r1 = null;
		RetailStageModelResponse r2 = null;
		RetailStageModelResponse r3 = null;
		List<RetailStageModelResponse> R1List = null;
		List<RetailStageModelResponse> R2List = null;
		List<RetailStageModelResponse> R3List = null;
		NativeQuery<?> query = null;

		String sqlQuery = "SELECT DISTINCT  Retail_Stage_Id, Retail_Stage_Main_Code,Retail_Stage_Sub_Value FROM SA_MST_ENQ_RETAIL_STAGE where IsActive='Y';\r\n"
				+ "";
		try {
			List<RetailFollowedStageResponseModel> followedStage = fetchRetailFollowStage(session, userCode, enquiryId);
			query = session.createNativeQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				R1List = new ArrayList<>();
				R2List = new ArrayList<>();
				R3List = new ArrayList<>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					if (row.get("Retail_Stage_Main_Code").equals("R1")) {
						r1 = new RetailStageModelResponse();
						r1.setRetailStageId((Integer) row.get("Retail_Stage_Id"));
						r1.setRetailCode((String) row.get("Retail_Stage_Main_Code"));
						r1.setRetailValue((String) row.get("Retail_Stage_Sub_Value"));
						if (followedStage != null) {
							/*
							 * int size = followedStage.stream() .filter(f ->
							 * f.getRetailStageId().equals((Integer) row.get("Retail_Stage_Id")))
							 * .collect(Collectors.toList()).size(); if (size == 1) { r1.setChecked(true); }
							 */
							Boolean match = followedStage.stream()
									.anyMatch(m -> m.getRetailStageId().equals((Integer) row.get("Retail_Stage_Id")));
							if (match) {
								r1.setChecked(true);
							}
						}

						R1List.add(r1);
					}
					if (row.get("Retail_Stage_Main_Code").equals("R2")) {
						r2 = new RetailStageModelResponse();
						r2.setRetailStageId((Integer) row.get("Retail_Stage_Id"));
						r2.setRetailCode((String) row.get("Retail_Stage_Main_Code"));
						r2.setRetailValue((String) row.get("Retail_Stage_Sub_Value"));
						if (followedStage != null) {
							Boolean match = followedStage.stream()
									.anyMatch(m -> m.getRetailStageId().equals((Integer) row.get("Retail_Stage_Id")));
							if (match) {
								r2.setChecked(true);
							}
						}
						R2List.add(r2);
					}
					if (row.get("Retail_Stage_Main_Code").equals("R3")) {
						r3 = new RetailStageModelResponse();
						r3.setRetailStageId((Integer) row.get("Retail_Stage_Id"));
						r3.setRetailCode((String) row.get("Retail_Stage_Main_Code"));
						r3.setRetailValue((String) row.get("Retail_Stage_Sub_Value"));
						if (followedStage != null) {
							Boolean match = followedStage.stream()
									.anyMatch(m -> m.getRetailStageId().equals((Integer) row.get("Retail_Stage_Id")));
							if (match) {
								r3.setChecked(true);
							}
						}
						R3List.add(r3);
					}

				}
				if (!R1List.isEmpty() && !R2List.isEmpty() && !R3List.isEmpty()) {
					mapData.put("R1List", R1List);
					mapData.put("R2List", R2List);
					mapData.put("R3List", R3List);
				}

			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return mapData;
	}

}
