package com.hitech.dms.web.model.goodsInTransitReport.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"ProfitCenter","productdivision","state","dealership","parentDealerCode","parentDealerLocation","vinNo","engineNo","chassisNo","model","variant","itemNo","itemDescription","mfgInvoiceNumber","mfgInvoiceDate","unitPrice","PONO","PODATE","LRNO","LRDATE","transporter","StockQuantity"})
public class GoodsInTransitReportResponse{
	
	private String ProfitCenter;
	private String productdivision;
	private String state;
	private String dealership;
	private String parentDealerCode;
	private String parentDealerLocation;
	private String vinNo;
	private String engineNo; 
	private String chassisNo;
	private String model;
	private String variant;
	private String itemNo;
	private String itemDescription;
	private String mfgInvoiceNumber;
	private String  mfgInvoiceDate;
	private BigDecimal unitPrice;
	private String PONO;
	private Date PODATE;
	private String LRNO;
	private Date LRDATE;
	private String transporter;
	private Integer  StockQuantity;
		
}