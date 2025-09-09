package com.hitech.dms.web.model.report.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "kpdName", "kpdCode", "invoiceDate", "invNumber", "partyCode","partyName", "zone", "stateDesc", "districtDesc", "productCategory", "productSubCategory", "partNumber", "partDescription", "qty", "rate", "value","hsnCode", "gstPercentage", "gstValue","totalValue", "custGstNo","status"})
public class InvoiceReportSearchRep implements Serializable {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String kpdName;
	
	private String kpdCode;
	
	private String invoiceDate;
	
	private String invNumber;
	

	private String  partyCode;
	
	private String partyName;
	
	
	
	private String zone;
	
	private String stateDesc;
	
	private String districtDesc;
	
	private String productCategory;
	
	private String productSubCategory;
	
	private String partNumber;
	
	private String partDescription;
	
	private BigDecimal qty;
	
	private BigDecimal rate;
	
	private BigDecimal value;
	
	private String hsnCode;
	
	private BigDecimal gstPercentage;
	
	private BigDecimal gstValue;
	
	private BigDecimal totalValue;
	
	private String custGstNo;
	
	private String status;
	
	
	

	
	


}
