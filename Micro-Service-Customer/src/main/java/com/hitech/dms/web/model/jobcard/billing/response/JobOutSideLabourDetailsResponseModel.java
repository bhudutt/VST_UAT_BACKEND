package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class JobOutSideLabourDetailsResponseModel {

	private BigInteger roBillId;
	private Integer outSideLBRDetailsId;
	private String billableType;
	private String labourCode;
	private String labourDescription;
	private String hsnCode;
	private BigDecimal rate;
	private BigDecimal disc;
	private BigDecimal discAmt;
	private BigDecimal netAmt;
	private BigDecimal cgst;
	private BigDecimal cgstAmt;
	private BigDecimal sgct;
	private BigDecimal sgstAmt;
	private BigDecimal igst;
	private BigDecimal igstAmt;
	private BigDecimal totalAmt;
	private Integer labourId;
	private BigInteger labourIds;
	private Integer BillableTypeId;
	private Integer roId;
	private BigDecimal hour;
	private BigDecimal amount;
}
