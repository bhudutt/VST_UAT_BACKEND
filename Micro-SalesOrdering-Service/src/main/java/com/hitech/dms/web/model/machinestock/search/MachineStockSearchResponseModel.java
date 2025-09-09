package com.hitech.dms.web.model.machinestock.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
@Data
public class MachineStockSearchResponseModel {
	
	private String zone;
	private String state;
	private String territory;
	private String dealership;
	private String branch;
	private String productDivision;
	private String model;
	private String itemNumber;
	private String itemDescription;
	private String variant;
	private String 	chassisNo;
	private String  vinNo;
	private String  engineNo;
	private String  mfgInvoiceNumber;
	private Date    mfgDate;
	private String    mfgInvoiceDate;
	//private String  sellingDealerCode;
	//private String  csbNumber;
	private String  registrationNumber;
	private Date    installationDate;
	private Date pdiInwardDate;
	private BigDecimal  unitPrice;
	private BigDecimal  unitPriceMrp;
	private Integer noOfDayStock;
	private String poNo;
	private Date poDate;
	private String transporter;
	private String lrNo;
	private Date lrDate;
	private String grnNo;
	private String grnDate;
	private String profitCenter;
	private String status;
	private String customermMobileNo;
	private String deliverychallanNo;
	private String deliverychallanDate;
	private String customerinvoiceNo;
	private String customerinvoiceDate;
	private String customerName;
	private Integer stockQuantity;
	private String  productGroup;
	private String area;
	//private Date    deliveryDate;
//	private BigInteger  originalCustomerId;
//	private BigInteger  latestCustomerId;

	private Integer stockQty;
	
	private String parentDealerCode;
	private String parentDealerLocation;
	
	
}
