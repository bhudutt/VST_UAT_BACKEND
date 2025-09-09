package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class InvoiceNumberResponse {

//	private BigInteger invoiceHdrId;
	private String invoiceNo;
	private Date invoiceDate;
	private String transporter;
	private String lrNo;
	private Date lrDate;
	private BigDecimal invoiceAmount;
	private String msg;
	private Integer statusCode;
	private String grnNumber;
	private String grnStatus;
	private BigInteger partyId;
	private String partyCode;
	private String partyName;
	private String poNumber; // if exist in pa_grn_hdr table
	private String productCategory; // if exist in pa_grn_hdr table
	private BigDecimal specialDiscount;
	private BigDecimal tcsPercent;
	private BigDecimal otherCharges;
	private BigDecimal otherChargesGst;

}
