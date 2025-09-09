package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class JobPartDetailsResponseModel {

	private BigInteger partDetailsId;
	private BigInteger roBillId;
	private String billableType;
	private String partNo;
	private String partDescription;
	private String uom;
	private String hsnCode;
	private BigDecimal unitPrice;
	private BigDecimal qty;
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
	private BigInteger requisitionId;
	private BigInteger roId;
	private Integer partBranchId;
	private BigInteger partBranchIds;
	private Integer bilableTypeId;
	private BigDecimal amount;
	
	
}
