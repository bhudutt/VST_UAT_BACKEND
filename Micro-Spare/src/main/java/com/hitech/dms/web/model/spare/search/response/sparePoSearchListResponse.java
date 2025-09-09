/**
 * 
 */
package com.hitech.dms.web.model.spare.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */

@Data
@JsonPropertyOrder({"poHdrId","action","poNumber","poCreationDate","dealerShipName","poStatus","partyCode","partyName",
		"poOn","poReleaseDate","productCategory","poType"})
public class sparePoSearchListResponse {
	private BigInteger poHdrId;
	private String action;
	private String poNumber;
	private String poCreationDate;
	private String poStatus;
	private String partyCode;
	private String partyName;
	private String poOn;
	private String poReleaseDate;
	//private String zone;
	//private String state;
	//private String territory;
	private String dealerShipName;
	private String productCategory;
	private String poType;
	
	//private String branchName;
	//private String baseAmount;
	//private BigDecimal totalGstAmount;
//	private String tcsPercent;
//	private BigDecimal totalTcsAmount;
	private BigDecimal totalPoAmount;
	//private String remarks;
	private String soNumber;
	private String soDate;
//	private String branch_district;
	private String invoiceNumber;
	private String invoiceDate;
//	private String area;
	private Integer totalRecords;

//	private Integer page;
//	private Integer size;

	
}

