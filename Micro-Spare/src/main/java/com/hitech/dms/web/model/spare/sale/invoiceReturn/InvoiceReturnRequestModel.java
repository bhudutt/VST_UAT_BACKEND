package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InvoiceReturnRequestModel {
	
	@JsonProperty(value = "header")
	private SpareInvoiceReturnHdrModel header;
	@JsonProperty(value="partDetails")
    private List<SpareSaleInvoiceReturnPartDtlModel> partDetails;
	private BigInteger hoUserId;
	private String remarks;
	private BigDecimal totalBasicValue;
	private BigDecimal totalTaxValue;
	private BigDecimal totalAllValue;
	private BigDecimal totalVocChargesValue;
	
}
