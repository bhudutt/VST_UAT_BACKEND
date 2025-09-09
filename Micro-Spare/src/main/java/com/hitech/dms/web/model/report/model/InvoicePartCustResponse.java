package com.hitech.dms.web.model.report.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class InvoicePartCustResponse {
	
	private BigInteger Id;
	
	private String customerName;
	
	private String customerCode;
	
	private String flag;

}
