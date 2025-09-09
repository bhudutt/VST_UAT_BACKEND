package com.hitech.dms.web.dao.spare.sale.invoiceCancel;

import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;


public interface SpareSaleInvoiceCancelDao {

	SpareSalesInvoiceResponse spareCancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode);
}
