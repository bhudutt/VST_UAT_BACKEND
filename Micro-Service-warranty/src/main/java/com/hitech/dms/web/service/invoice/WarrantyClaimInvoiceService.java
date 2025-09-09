package com.hitech.dms.web.service.invoice;

import java.math.BigInteger;

import javax.transaction.Transactional;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.invoice.WarrantyClaimInvoiceHdr;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.invoice.CreditNoteSearchRequestDto;
import com.hitech.dms.web.model.invoice.InvoiceSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Transactional
public interface WarrantyClaimInvoiceService {
	
	ApiResponse<?> fetchInvoiceDetails(BigInteger wcrId, String userCode);
	
	ApiResponse<?> createWCRInvoice(String authorizationHeader, String userCode,WarrantyClaimInvoiceHdr requestModel);
	
	ApiResponse<?>  autoSearchInvoiceNo(String invoiceNo);
	
	ApiResponse<?>  autoSearchWcrNo(String wcrNo);
	
	ApiResponse<?>  invoiceSearchList(String userCode, InvoiceSearchRequestDto requestModel);
	
	ApiResponse<?> viewInvoice(BigInteger invoiceId, String userCode);
	
	ApiResponse<?>  creditNoteSearchList(String userCode, CreditNoteSearchRequestDto requestModel);

}
