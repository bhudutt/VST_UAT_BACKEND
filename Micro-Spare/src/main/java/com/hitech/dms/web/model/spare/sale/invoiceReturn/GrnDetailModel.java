package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class GrnDetailModel {

	private String grnNumber;
	private  String GrnDate;
	private String grnFrom;
	private String invoiceNo;
	private String invoiceDate;
	private String spareSaleReturnInvoiceNo;
	private String spareSaleReturnInvoiceDate;
	
	
}
