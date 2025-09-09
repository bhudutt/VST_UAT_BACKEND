package com.hitech.dms.web.model.machinestock.search;


import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class MachineStockListResponseModel {

	
	private String 	chassisNo;
	private String  vinNo;
	private String  engineNo;
	private Date    mfgDate;
	private String  mfgInvoiceNumber;
	private Date    mfgInvoiceDate;
	private String  sellingDealerCode;
	private String  csbNumber;
	private String  registrationNumber;
	private Date    installationDate;
	private Double  unitPrice;
	private Date    deliveryDate;
	private BigInteger  originalCustomerId;
	private BigInteger  latestCustomerId;
	private String  productGroup;
	private String itemNumber;
	private String itemDescription;
	private String variant;
	private Integer stockQuantity;
	
}
