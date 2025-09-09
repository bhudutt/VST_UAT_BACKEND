package com.hitech.dms.web.model.advancereport;

import java.math.BigInteger;

import lombok.Data;

@Data
public class FinancierResponseListModel {

	private BigInteger financierId;
	private BigInteger financierTypeId;
	private String financierName;
	
}
