/**
 * 
 */
package com.hitech.dms.web.model.enquiry.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EnquiryReportWithDealerWiseResponse {

	private BigInteger stateId;
	private String state;	
	private String cluster;
	private String territoryManager;
	private String dealerName;
	private String dealerLocation;
	private String dsp;
	private String model;
	private String itemNo;
	private int enqMonthToDate;
	private int deliveriesForMonth;
	private int openingHot;
	private int openingWarm;
	private int openingCold;
	private int openingOverdue;
	private int totalOpeningEnq;
	private int lostEnquiries;
	private int droppedEnq;
	private int currentHotEnq;
	private int currentWarmEnq;
	private int currentColdEnq;
	private int currentOverdueEnq;
	private int totalCurrentEnq;


}
