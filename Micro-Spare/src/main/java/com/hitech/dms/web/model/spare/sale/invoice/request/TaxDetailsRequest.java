package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class TaxDetailsRequest {

	private BigDecimal taxableAmount;
	private BigInteger partId;
	private BigInteger qty;
	private BigInteger dealerId;
	private String partyCode;
	private BigInteger pinId;
	private String flag;
}
