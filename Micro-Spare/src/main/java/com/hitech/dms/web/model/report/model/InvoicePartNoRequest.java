package com.hitech.dms.web.model.report.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class InvoicePartNoRequest {
	
	private String searchText;
	
	private BigInteger branchId;

}
