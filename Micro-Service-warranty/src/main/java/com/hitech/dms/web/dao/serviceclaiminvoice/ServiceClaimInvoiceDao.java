package com.hitech.dms.web.dao.serviceclaiminvoice;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchRequestDto;


@Transactional
public interface ServiceClaimInvoiceDao {

	
	List<?> serviceClaimInvoiceSearch(Session session, String userCode, ServiceClaimInvoiceSearchRequestDto requestModel);
	
	List<?> viewServiceClaimInvoice(Session session, BigInteger invoiceId, Integer flag);

	
	List<?> autoGetClaimInvoiceNo(Session session, String claimInvNoStr);
	
	List<?> viewServiceClaimInvoiceForSave(Session session, BigInteger claimId, Integer flag);
	
	List<?> serviceClaimCreditNoteSearch(Session session, String userCode, ServiceClaimInvoiceSearchRequestDto requestModel);


}
