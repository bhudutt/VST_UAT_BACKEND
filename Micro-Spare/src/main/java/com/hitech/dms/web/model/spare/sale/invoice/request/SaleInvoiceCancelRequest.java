package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;

import lombok.Data;

@Data
public class SaleInvoiceCancelRequest {

	private BigInteger invoiceSaleId;
	private BigInteger referenceDocumentId;
	private String cancelRemarks;
	private BigInteger branchId;
	private BigInteger poHdrId;
	private BigInteger coHdrId;
	private BigInteger dcHdrId;
	private List<PartDetailRequest> partDetailRequest;
}
