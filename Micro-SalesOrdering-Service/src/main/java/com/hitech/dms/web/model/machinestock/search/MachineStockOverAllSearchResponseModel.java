package com.hitech.dms.web.model.machinestock.search;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"profitCenter","state","parentDealerCode","dealership","parentDealerLocation","productDivision","model","variant","itemNumber","itemDescription","vinNo","engineNo","chassisNo","stockQuantity","unitPrice","unitPriceMrp","mfgInvoiceNumber","mfgInvoiceDate","grnNo","grnDate","noOfDayStock","status"})
@JsonIgnoreProperties({"stockQty"})
public class MachineStockOverAllSearchResponseModel {
	
	private String state;
	private String dealership;
	private String productDivision;
	private String model;
	private String itemNumber;
	private String itemDescription;
	private String variant;
	private String 	chassisNo;
	private String  vinNo;
	private String  engineNo;
	private String  mfgInvoiceNumber;
	private String    mfgInvoiceDate;
	private BigDecimal  unitPrice;
	private BigDecimal  unitPriceMrp;
	private Integer noOfDayStock;
	private String grnNo;
	private String grnDate;
	private String profitCenter;
	private String status;
	private Integer stockQuantity;
	
	

	private Integer stockQty;
	
	@JsonProperty("Dealer Code")
	private String parentDealerCode;
	@JsonProperty("Dealer Location")
	private String parentDealerLocation;
	
	
}
