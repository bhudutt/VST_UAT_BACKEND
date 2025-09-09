package com.hitech.dms.web.dao.serviceclaiminvoice;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchRequestDto;

@SuppressWarnings("deprecation")
@Repository
public class ServiceClaimInvoiceDaoImpl implements ServiceClaimInvoiceDao{
	
	@Override
	public List<?> serviceClaimInvoiceSearch(Session session, String userCode, ServiceClaimInvoiceSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_SERVICE_CLAIM_INVOICE_SEARCH] :invoiceNo, :userCode, :claimNo, :claimTypeId, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceNo", requestModel.getInvoiceNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("claimNo", requestModel.getClaimNo());
	        query.setParameter("claimTypeId", requestModel.getClaimTypeId());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());

	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> viewServiceClaimInvoice(Session session, BigInteger invoiceId, Integer flag) {
		String sqlQuery = "exec [SV_SERVICE_CLAIM_INVOICE_VIEW] :invoiceId, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceId", invoiceId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> autoGetClaimInvoiceNo(Session session, String claimInvNoStr) {
		String sqlQuery = "exec [sp_get_auto_claim_invoice_no] :claimInvNoStr";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("claimInvNoStr", claimInvNoStr);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> viewServiceClaimInvoiceForSave(Session session, BigInteger claimId, Integer flag) {
		String sqlQuery = "exec [SV_SERVICE_CLAIM_INVOICE_VIEW_FOR_SAVE] :claimId, :flag";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("claimId", claimId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> serviceClaimCreditNoteSearch(Session session, String userCode, ServiceClaimInvoiceSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_SERVICE_CLAIM_CREDIT_NOTE_SEARCH] :invoiceNo, :userCode, :claimNo, :claimTypeId, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceNo", requestModel.getInvoiceNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("claimNo", requestModel.getClaimNo());
	        query.setParameter("claimTypeId", requestModel.getClaimTypeId());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());

	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

}
