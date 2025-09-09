package com.hitech.dms.web.dao.invoice;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.invoice.CreditNoteSearchRequestDto;
import com.hitech.dms.web.model.invoice.InvoiceSearchRequestDto;

/**
 * @author mahesh.kumar
 */
@Repository
public class WarrantyClaimInvoiceDaoImpl implements WarrantyClaimInvoiceDao{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<?> fetchInvoiceDetails(Session session, BigInteger wcrId, String userCode, int flag) {
		
		String sqlQuery = "exec [SP_WA_GET_INVOICE_DETAIL] :WCRID, :userCode, :FLAG";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("WCRID", wcrId);
	        query.setParameter("userCode", userCode);
	        query.setParameter("FLAG", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
	
	
	@Override
	public List<?> autoSearchInvoiceNo(Session session, String invoiceNo) {
	    String sqlQuery = "exec [sv_wa_invoice_autosearch_invoice_no] :invoiceNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceNo", invoiceNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> autoSearchWcrNo(Session session, String wcrNo) {
	    String sqlQuery = "exec [sv_wa_invoice_autosearch_wcr_no] :wcrNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("wcrNo", wcrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	
	@Override
	public List<?> invoiceSearchList(Session session, String userCode, InvoiceSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_WA_Invoice_Search_Details] :invoiceNo, :userCode, :wcrNo, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceNo", requestModel.getInvoiceNo()); // Set appropriate values from requestModel
	        query.setParameter("userCode", userCode);
	        query.setParameter("wcrNo", requestModel.getWcrNo());
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
	public List<?> viewInvoice(Session session, BigInteger invoiceId, String userCode, int flag) {
		
		String sqlQuery = "exec [SP_WA_VIEW_INVOICE_DETAIL] :invoiceId, :userCode, :flag";
		Query<?> query = null;
	    List<?> data = null;
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceId", invoiceId);
	        query.setParameter("userCode", userCode);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return data;
	}
	
	@Override
	public List<?> creditNoteSearchList(Session session, String userCode, CreditNoteSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_WA_Invoice_Credit_Notes_Search_Details] :invoiceNo, :wcrNo, :userCode, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceNo", requestModel.getInvoiceNo());
	        query.setParameter("wcrNo", requestModel.getWcrNo());
	        query.setParameter("userCode", userCode);
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
