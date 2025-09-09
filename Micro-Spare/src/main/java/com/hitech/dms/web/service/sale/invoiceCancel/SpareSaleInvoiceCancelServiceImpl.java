package com.hitech.dms.web.service.sale.invoiceCancel;

import java.math.BigInteger;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.spare.sale.invoice.SpareSaleInvoiceDao;
import com.hitech.dms.web.dao.spare.sale.invoice.SpareSaleInvoiceDaoImpl;
import com.hitech.dms.web.dao.spare.sale.invoiceCancel.SpareSaleInvoiceCancelDao;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;

@Service
public class SpareSaleInvoiceCancelServiceImpl implements SpareSaleInvoiceCancelService {
	private static final Logger logger = LoggerFactory.getLogger(SpareSaleInvoiceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	SpareSaleInvoiceCancelDao spareSaleInvoiceCancelDao;
	
	@Autowired
	SpareSaleInvoiceDao spareSaleInvoiceDao;

	@Autowired
	CommonDao commonDao;
	
	@Override
	public SpareSalesInvoiceResponse cancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode) {
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = 
				spareSaleInvoiceCancelDao.spareCancelInvoice(saleInvoiceCancelRequest, userCode);
		
		if (spareSalesInvoiceResponse.getStatusCode() == 201) {
			BigInteger referenceDocId = saleInvoiceCancelRequest.getReferenceDocumentId();

			boolean isCustomerOrder = referenceDocId.equals(BigInteger.valueOf(2));
			boolean isDcOrder = referenceDocId.equals(BigInteger.valueOf(3));

			boolean isPoOrder = referenceDocId.equals(BigInteger.valueOf(4))
					|| referenceDocId.equals(BigInteger.valueOf(5))
					|| referenceDocId.equals(BigInteger.valueOf(6));
			
			if (isPoOrder) {
				spareSaleInvoiceDao.updateInPoHdr("PO", saleInvoiceCancelRequest.getPoHdrId(), userCode);
			}
			String flag = isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;

			for (PartDetailRequest partDetailRequest : saleInvoiceCancelRequest.getPartDetailRequest()) {
				
				if (isCustomerOrder || isDcOrder) {
					spareSaleInvoiceDao.updateInOrderStatus(flag, partDetailRequest.getCustomerOrderHdrId(), userCode);
//				} else if (isDcOrder) {
//					row = spareSaleInvoiceDao.updateInDC(partDetailRequest.getCustomerOrderHdrId(), userId);
				}
			}
		}
		
		return spareSalesInvoiceResponse;
	}

}
