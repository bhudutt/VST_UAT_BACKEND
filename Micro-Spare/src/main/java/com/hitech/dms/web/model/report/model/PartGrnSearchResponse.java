package com.hitech.dms.web.model.report.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"dealerCode", "dealerName", "invoiceNo", "grnNumber", "grnDate", "prodCategory", "partNumber", "partDesc", "qty","unitPrice", "totalValue", "hsnCode", "gstPercentage", "gstValue", "grossValue"})
public class PartGrnSearchResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String dealerCode;
	
	private String dealerName;
	
	private String invoiceNo;
	
	private String grnNumber;
	
	private Date  grnDate;
	
	private String prodCategory;
	
	private String partNumber;
	
	private String partDesc;
	
	private BigDecimal qty;
	
	private BigDecimal unitPrice;
	
	private BigDecimal totalValue;
	
	private String hsnCode;
	
	private BigDecimal gstPercentage;
	
	private BigDecimal gstValue;
	
	private BigDecimal grossValue;

}
