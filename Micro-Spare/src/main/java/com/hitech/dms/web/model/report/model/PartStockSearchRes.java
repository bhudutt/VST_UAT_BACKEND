package com.hitech.dms.web.model.report.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartStockSearchRes {

	
private String dealerCode;
private String dealerName;
private String partnumber;
private String partdesc;
private String HSNcode;
private String storeName;
private String binLocation;
private BigDecimal openingQty;
private BigDecimal openingRate;
private BigDecimal openingValue;
private BigDecimal receiptQty;
private BigDecimal receiptRate;
private BigDecimal receiptValue;
private BigDecimal issuedQty;
private BigDecimal issueRate;
private BigDecimal issueValue;
private BigDecimal balanceQty;
private BigDecimal balanceRate;
private BigDecimal balancevalue;
private BigDecimal currentStock;//added on 11-09-24
}
