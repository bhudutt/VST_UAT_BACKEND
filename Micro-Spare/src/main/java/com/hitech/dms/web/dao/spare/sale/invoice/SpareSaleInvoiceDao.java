package com.hitech.dms.web.dao.spare.sale.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SalesInvoiceDCResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.TaxDetailsRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.PartTaxCalCulationResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;


public interface SpareSaleInvoiceDao {

	HashMap<BigInteger, String> fetchReferenceDocList(Integer dealerId, String userCode);

	List<SalesInvoiceDCResponse> deliveryChallanDtl(Integer partyBranchId, String reqType);

	List<COpartDetailResponse> getDcPartDetail(String deliveryChallanNumber);

	SpareSalesInvoiceResponse createSpareSaleInvoice(String userCode, SpareSalesInvoiceRequest salesInvoiceRequest);

	HashMap<BigInteger, String> searchInvoiceList(String searchText, String userCode);

	List<SpareSalesInvoiceResponse> fetchInvoiceList(String invoiceNumber,
			Date fromDate, Date toDate, String userCode,
			 Integer page, Integer size);

	HashMap<BigInteger, String> searchDCNumber(String searchText, String categoryCode, String userCode);

	SpareSalesInvoiceResponse fetchHdr(BigInteger invoiceId);

	PartDetailRequest fetchDtl(BigInteger invoiceId, Integer flag);

	SpareSalesInvoiceResponse spareCancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode);

	Integer updateInCustomerOrderDtl(PartDetailRequest partDetailRequest, BigInteger userId);

	ApiResponse<List<SparePoHdrDetailsResponse>> fetchPoHdrDetailsById(String userCode, Integer poHdrId);

	List<partSerachDetailsByPohdrIdResponseModel> fetchPoPartDetailsByPohdrId(String userCode, Integer poHdrId);

	Integer updateInOrderStatus(String flag, BigInteger customerHdrId, String userCode);
	
//	Integer updateInDC(BigInteger dcHdrId, BigInteger userId);

	Integer updateInPoHdr(String flag, BigInteger poHdrId, String userCode);

	PartTaxCalCulationResponse fetchTaxDetails(TaxDetailsRequest taxDetailsRequest,
			String userCode);

	void updateQrCodeStatus(BigInteger invoiceId, String userCode);

//	HashMap<BigInteger, String> fetchMRPList(Integer id, String userCode);

//	Integer updateInvoiceQtyInOrder(PartDetailRequest partDetailRequest, String flag, String userCode);

	
}
