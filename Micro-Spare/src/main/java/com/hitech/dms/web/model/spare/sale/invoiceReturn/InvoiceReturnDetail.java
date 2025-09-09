package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceReturnDetail {

	private GrnDetailModel grnDetail;
	private ClaimDetailModel claimDetail;
	private List<GrnHeaderResponse> headerResponse;
	private List<PartDetailsModel> partDetailList;
	BigDecimal basicAmountTotal;
	BigDecimal gstAmountTotal;
	BigDecimal totalAmountTotal;
	BigDecimal totalVorCharges;

	int statusCode;
	String statusMessage;

}
