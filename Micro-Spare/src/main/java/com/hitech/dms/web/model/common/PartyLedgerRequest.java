package com.hitech.dms.web.model.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PartyLedgerRequest {

	private BigInteger partyId;
	private String transactionName;
	private BigInteger transactionId;
	private Date transactionDate;
	private BigDecimal transactionAmount;
	private String transactionType;
}
