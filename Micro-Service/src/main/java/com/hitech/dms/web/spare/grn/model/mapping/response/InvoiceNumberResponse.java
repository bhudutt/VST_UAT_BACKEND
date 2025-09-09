package com.hitech.dms.web.spare.grn.model.mapping.response;

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
}
