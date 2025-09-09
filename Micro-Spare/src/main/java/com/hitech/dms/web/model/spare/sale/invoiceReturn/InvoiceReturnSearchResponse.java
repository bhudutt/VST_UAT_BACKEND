package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceReturnSearchResponse {
	
	private List<SpareInvoiceRetunSearchModel> searchList;
	private List<InvoiceReturnParts> partList;
	private String message;
	private int statusCode;
	private Integer totalRecord;
	private BigDecimal totalBasicAmount;
	private BigDecimal totalGstAmount;
	private BigDecimal totalAmount;
	private BigDecimal totalVorCharges;

}
