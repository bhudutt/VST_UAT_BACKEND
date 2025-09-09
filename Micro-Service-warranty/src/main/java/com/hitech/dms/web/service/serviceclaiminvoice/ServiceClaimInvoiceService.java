package com.hitech.dms.web.service.serviceclaiminvoice;

import java.math.BigInteger;

import javax.transaction.Transactional;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.serviceclaiminvoice.ServiceClaimInvoiceHdr;
import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchRequestDto;

@Transactional
public interface ServiceClaimInvoiceService {
	
	ApiResponse<?> saveServiceClaimInvoice(String userCode, ServiceClaimInvoiceHdr requestModel);
	
	ApiResponse<?> autoGetClaimInvoiceNo(String claimNoString);
	
	ApiResponse<?> serviceClaimInvoiceSearch(String userCode, ServiceClaimInvoiceSearchRequestDto requestModel);
	
	ApiResponse<?> viewServiceClaimInvoiceForSave(BigInteger claimId);
	
	ApiResponse<?> viewServiceClaimInvoice(BigInteger invoiceId);
	
	ApiResponse<?> serviceClaimCreditNoteSearch(String userCode, ServiceClaimInvoiceSearchRequestDto requestModel);

}
