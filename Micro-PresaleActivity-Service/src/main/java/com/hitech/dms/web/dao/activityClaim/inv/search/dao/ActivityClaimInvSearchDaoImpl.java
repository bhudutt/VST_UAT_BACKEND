/**
 * 
 */
package com.hitech.dms.web.dao.activityClaim.inv.search.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimAutoSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimInvSearchRequest;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimAutoSearchResponseModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimInvSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityClaimInvSearchDaoImpl implements ActivityClaimInvSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityClaimInvSearchResponseModel> fetchActivityClaimInvSearchList(String userCode,
			ActivityClaimInvSearchRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimInvSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActivityClaimInvSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_SEARCH_ACTIVITYCLAIMINV] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :activityClaimInvoiceNumber, :fromDate, :toDate, :isIncludeInActive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("orgHierId", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("activityClaimInvoiceNumber", requestModel.getActivityClaimInvoiceNumber());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("isIncludeInActive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityClaimInvSearchResponseModel>();
				ActivityClaimInvSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityClaimInvSearchResponseModel();
					responseModel.setSrlNo((BigInteger) row.get("Sr_No"));
					responseModel.setAction((String) row.get("Action"));
					responseModel.setId((BigInteger) row.get("ActivityClaimInvId"));
					responseModel.setActivityClaimInvNumber((String) row.get("ActivityClaimInvNumber"));
					responseModel.setStatus((String) row.get("gstStatus"));
					responseModel.setClaimInvoiceDate((String) row.get("ClaimInvoiceDate"));
					responseModel.setActivityPlanNo((String) row.get("ActivityPlanNo"));
					responseModel.setActivityPlanDate((String) row.get("ActivityPlanDate"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setProfitCenterDesc((String) row.get("ProfitCenterDesc"));
					responseModel.setFinancialYear((Integer) row.get("FinancialYear"));
					responseModel.setFinancialMonth((String) row.get("FinancialMonth"));
					responseModel.setTotalBudget((BigDecimal) row.get("TotalBudget"));
					responseModel.setTotalApprovedBudget((BigDecimal) row.get("TotalApprovedBudget"));
					responseModel.setTotalReimbursementClaim((BigDecimal) row.get("TotalReimbursementClaim"));
					responseModel.setApprovedClaimAmount((BigDecimal) row.get("ApprovedClaimAmount"));
					responseModel.setGstPer((BigDecimal) row.get("GstPer"));
					responseModel.setGstAmount((BigDecimal) row.get("GstAmount"));
					responseModel.setTotalInvoiceAmount((BigDecimal) row.get("TotalInvoiceAmount"));
					String isReceivedHardCopy = (String) row.get("IsReceivedHardCopy");
					if (isReceivedHardCopy != null && isReceivedHardCopy.equals("Y")) {
						responseModel.setReceivedHardCopy(true);
					} else {
						responseModel.setReceivedHardCopy(false);
					}
					responseModel.setCreditNoteNumber((String) row.get("CreditNoteNumber"));
					responseModel.setCreditNoteDate((String) row.get("CreditNoteDate"));
					responseModel.setCreditNoteAmount((BigDecimal) row.get("CreditNoteAmount"));
					responseModel.setRemarks((String)row.get("remarks"));
					String isCreditNotePdfLink = (String) row.get("IsCreditNotePdfLink");
					if (isCreditNotePdfLink != null && isCreditNotePdfLink.equals("Y")) {
						responseModel.setCreditNotePdfLink(true);
					} else {
						responseModel.setCreditNotePdfLink(false);
					}
					responseModel.setCustomerInvoiceDate((String)row.get("customer_invoice_date"));
					responseModel.setCustomerInvoiceNo((String)row.get("customer_invoice_no"));
					
					responseModel.setFinalSubmit("Y".equals((String)row.get("final_submit")) ? "Submitted" : "Pending");
					responseModel.setApprovedDate((String)row.get("Level_1_Approved_Date"));
					
					responseModelList.add(responseModel);
				}
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
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ActivityClaimAutoSearchResponseModel> fetchActivityClaimInvAutoSearchList(String userCode,
			ActivityClaimAutoSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityClaimInvAutoSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<ActivityClaimAutoSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SACT_AutoSearch_ByClaimNO] :userCode, :searchText, :dealerId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", requestModel.getSearchText());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityClaimAutoSearchResponseModel>();
				ActivityClaimAutoSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityClaimAutoSearchResponseModel();
					responseModel.setId((BigInteger) row.get("Id"));
					responseModel.setDocNumber((String) row.get("DocNumber"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerId((BigInteger) row.get("dealerId"));
					responseModelList.add(responseModel);
				}
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
		return responseModelList;
	}
}
