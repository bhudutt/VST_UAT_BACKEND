package com.hitech.dms.web.service.sale.invoice;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SalesInvoiceDCResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.TaxDetailsRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.PartTaxCalCulationResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;

public interface SpareSaleInvoiceService {

	HashMap<BigInteger, String> fetchReferenceDocList(Integer dealerId, String userCode);

	SpareSalesInvoiceResponse createSpareSaleInvoice(String userCode, SpareSalesInvoiceRequest salesInvoiceRequest);

	List<SalesInvoiceDCResponse> deliveryChallanDtl(Integer partyBranchId, String reqType);

	List<COpartDetailResponse> getDcPartDetail(String deliveryChallanNumber);

	HashMap<BigInteger, String> searchInvoiceList(String searchText, String userCode);

	List<SpareSalesInvoiceResponse> fetchInvoiceList(String invoiceNumber,
			Date fromDate, Date toDate, String userCode,
			 Integer page, Integer size);

	HashMap<BigInteger, String> searchDCNumber(String searchText, String categoryCode, String userCode);

	SpareSalesInvoiceResponse fetchHdrAndDtl(BigInteger invoiceId, String userCode);

	SpareSalesInvoiceResponse cancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode);

	ApiResponse<?> fetchPoHdrAndPartDetails(String userCode, Integer poHdrId);

	PartTaxCalCulationResponse fetchTaxDetails(TaxDetailsRequest taxDetailsRequest,
			String userCode);

//	HashMap<BigInteger, String> fetchMRPList(Integer id, String userCode);

}
