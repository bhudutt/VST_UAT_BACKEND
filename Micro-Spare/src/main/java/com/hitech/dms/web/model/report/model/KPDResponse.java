package com.hitech.dms.web.model.report.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class KPDResponse {
	
	private BigInteger Id;
	
	private String customerName;
	
	private String customerCode;
}
