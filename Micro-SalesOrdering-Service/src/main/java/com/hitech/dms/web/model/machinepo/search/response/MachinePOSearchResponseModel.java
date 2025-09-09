/**
 * 
 */
package com.hitech.dms.web.model.machinepo.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOSearchResponseModel {
	private BigInteger poHdrId;
	private String action;
	private String zone;
	private String area;
	private String dealerShip;
	private String profitCenter;
	private String poNumber;
	private String poDate;
	private String poStatus;
	private String poReleasedDate;
	private String poOn;
	private String partyCode;
	private String partyName;
	private String rso;
	private String remarks;
	private String soNumber;
	private String soDate;
	private BigDecimal baseAmount;
	private BigDecimal totalGstAmount;
	private BigDecimal tcsPer;
	private BigDecimal tcsAmount;
	private BigDecimal totalPOAmount;
	private String invoiceNumber;
	private String invoiceDate;
}
