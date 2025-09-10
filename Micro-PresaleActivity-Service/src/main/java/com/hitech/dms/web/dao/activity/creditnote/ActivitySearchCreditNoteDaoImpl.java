package com.hitech.dms.web.dao.activity.creditnote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.hitech.dms.web.model.activitycreditnote.search.request.SearchActivityCreditNoteRequestModel;
import com.hitech.dms.web.model.activitycreditnote.search.response.SearchActivityCreditNoteResponseModel;
import com.hitech.dms.web.model.activitycreditnote.search.response.SearchActivityCreditNoteResultResponseModel;



@Repository
public class ActivitySearchCreditNoteDaoImpl implements ActivitySearchCreditNoteDao {

	private static final Logger logger = LoggerFactory.getLogger(ActivitySearchCreditNoteDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public SearchActivityCreditNoteResultResponseModel searchlistActivityCreditNoteList(String userCode,
			SearchActivityCreditNoteRequestModel searchActivityCreditNoteRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"searchlistActivityCreditNoteList invoked.." + userCode + " " + searchActivityCreditNoteRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		SearchActivityCreditNoteResultResponseModel responseModel = null;
		List<SearchActivityCreditNoteResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [sp_sa_credit_note] :userCode, :FromDate, :ToDate, :vendorinvoiceno, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("FromDate", searchActivityCreditNoteRequestModel.getFromDate());
			query.setParameter("ToDate", searchActivityCreditNoteRequestModel.getToDate());
			query.setParameter("vendorinvoiceno", searchActivityCreditNoteRequestModel.getVendorinvoiceno());
			query.setParameter("page", searchActivityCreditNoteRequestModel.getPage());
			query.setParameter("size", searchActivityCreditNoteRequestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SearchActivityCreditNoteResponseModel>();
				responseModel = new SearchActivityCreditNoteResultResponseModel();
				SearchActivityCreditNoteResponseModel searchModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					searchModel = new SearchActivityCreditNoteResponseModel();
					
					searchModel.setActivityNumber((String) row.get("ActivityNumber"));
					searchModel.setActivityCreationDate((String) row.get("ActivityCreationDate"));
					searchModel.setActivityMonth((Integer) row.get("ActivityMonth"));
					searchModel.setActivityYear((Integer) row.get("ActivityYear"));
					searchModel.setTotalBudget((BigDecimal) row.get("TotalBudget"));
					searchModel.setApprovedBudget((BigDecimal) row.get("ApprovedBudget"));
					searchModel.setReimbursementClaim((BigDecimal) row.get("ReimbursementClaim"));
					searchModel.setDescription((String)row.get("Description"));  
					searchModel.setCreditNoteNo((String) row.get("CreditNoteNo"));
					searchModel.setCreditNoteAmount((BigDecimal) row.get("CreditNoteAmount")); 
					searchModel.setCreditNoteDate((Date) row.get("CreditNoteDate"));
					searchModel.setActivityInvoiceNo((String) row.get("ActivityClaimInvNumber"));
					searchModel.setActivityInvoiceDate((Date) row.get("ClaimInvoiceDate"));
//					searchModel.setVendorCode((String) row.get("VendorCode"));
					searchModel.setVendorinvoiceno((String) row.get("vendorinvoiceno"));
					
					
					recordCount++;
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					
					responseModelList.add(searchModel);
				}

				responseModel.setRecordCount(recordCount);
				responseModel.setSearchResult(responseModelList);
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
		return responseModel;
	}
}

