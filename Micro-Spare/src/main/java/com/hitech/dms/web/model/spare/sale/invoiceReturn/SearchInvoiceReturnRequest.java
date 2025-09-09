package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import lombok.Data;

@Data
public class SearchInvoiceReturnRequest {

	
	private String fromDate;
	private String toDate;
	private String claimNo;
	private String mrnNo;
	private Integer invoiceReturnId;
	private String invoiceReturnNo;
	private String returnType;
	private int page;
	private int size;
	
}
