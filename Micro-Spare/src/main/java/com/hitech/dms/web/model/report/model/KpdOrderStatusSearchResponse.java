package com.hitech.dms.web.model.report.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Data
@JsonPropertyOrder({"customerName", "customerCode", "kpdCode", "kpdName", "poNumber", "orderDate", "partNumber", "partDesc", "orderQty","shipQty", "balanceQty", "poStatus", "poValue", "invoiceValue", "balanceValue", "percentage", "ageingDay"})
public class KpdOrderStatusSearchResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private String customerName;
	
	private String customerCode;
	
	private String kpdCode;
	
	private String kpdName;
	
	private String poNumber;
	
	private Date  orderDate;
	
	private String partNumber;
	
	private String partDescription;
	
	private BigDecimal orderQty;
	
	private BigDecimal shipQty;
	
	private BigDecimal balanceQty;
	
	private String poStatus;
	
	private BigDecimal poValue;
	
	private BigDecimal invoiceValue;
	
	private BigDecimal balanceValue;
	
	private BigDecimal percentage;
	
	private BigDecimal ageingDay;
}
