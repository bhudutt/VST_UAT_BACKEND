package com.hitech.dms.web.model.spare.sale.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class SpareInvoicePriceResponse {
	
	private BigInteger partStatePriceId;
	private BigInteger partId;
	private BigDecimal dealerMrp;
	private BigDecimal discount;
	private BigDecimal basicUnitPrice;
	
	

}
