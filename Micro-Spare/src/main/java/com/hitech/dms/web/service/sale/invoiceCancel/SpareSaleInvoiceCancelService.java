package com.hitech.dms.web.service.sale.invoiceCancel;

import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;

public interface SpareSaleInvoiceCancelService {

	SpareSalesInvoiceResponse cancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode);

}
