package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import lombok.Data;

@Data
public class InvoiceReturnPdfRequest {
	
	private String MRNNumber;
	private String invoiceReturnNo;
	private String claimGenerationNumber;
	private String flag;
	private String returnType;

}
