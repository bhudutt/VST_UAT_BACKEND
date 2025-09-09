package com.hitech.dms.web.dao.invoice;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.invoice.CreditNoteSearchRequestDto;
import com.hitech.dms.web.model.invoice.InvoiceSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Transactional
public interface WarrantyClaimInvoiceDao {
	
	List<?> fetchInvoiceDetails(Session session, BigInteger wcrId, String userCode, int flag);
	
	List<?> autoSearchInvoiceNo(Session session, String invoiceNo);
	
	List<?> autoSearchWcrNo(Session session, String wcrNo);
	
	List<?> invoiceSearchList(Session session, String userCode, InvoiceSearchRequestDto requestModel);
	
	List<?> viewInvoice(Session session, BigInteger invoiceId, String userCode, int flag);
	
	List<?> creditNoteSearchList(Session session, String userCode, CreditNoteSearchRequestDto requestModel);

}
