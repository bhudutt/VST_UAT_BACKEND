package com.hitech.dms.web.model.report.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"poDate", "poNumber", "supplier", "profitCentre", "categoryName", "location", "partNo", "partDesc", "orderQty","rate", "value", "supplyQty", "pendingQty", "supplyValue", "status"})

public class PoStatusSearchResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Date poDate;
	
	private String poNumber;
	
	private String supplier;
	
	private String profitCentre;
	
	private String categoryName;
	
	private String location;
	
	private String partNo;
	
	private String partDesc;
	
	private BigDecimal orderQty;
	
	private BigDecimal rate;
	
	private BigDecimal value;
	
	private BigDecimal supplyQty;
	
	private BigDecimal pendingQty;
	
	private BigDecimal supplyValue;
	
	private String status;

}
