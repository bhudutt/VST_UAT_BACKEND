package com.hitech.dms.web.model.dc.search.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({"id1","id2","dealerCode","dealerName","pcDesc","dcNumber","dcStatus","dcDate","allotNumber","allotDate","allotStatus","enquiryNo","enquiryDate","id3","customerName","machineItemId","registrationNumber","installationDate"})
public class StockDetailsResponseModel {
	private BigInteger id; // dcId
	private BigInteger id1;
	private BigInteger id2;
	private String action;
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String dcNumber;
	private String dcStatus;
	private String dcDate;
	private String allotNumber;
	private String allotDate;
	private String allotStatus;
	private String enquiryNo;
	private String enquiryDate;
	private BigInteger id3; // customerId
	private String customerName;
	private BigInteger vinId; 
	private Integer machineItemId; 
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private String mfgInvoiceNumber;
	private String mfgInvoiceDate;
	private String registrationNumber;
	private String installationDate;
	private Double unitPrice;
	private String itemNo;
	private String itemDesc;
	private String mfgDate;
	private String sellingDealerCode;
	private String csbNumber;
	private String saleDate;
	

}
