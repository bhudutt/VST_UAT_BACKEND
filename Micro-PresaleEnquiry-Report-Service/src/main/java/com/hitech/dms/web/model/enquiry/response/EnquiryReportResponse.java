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
public class EnquiryReportResponse {
//	private String stateDesc;
//	private String dealerName;
//	private String location;
//	private String DSP;
//	private String modelName;
//	private int itemNo;
//	private int enqMonthToDate;
//	private int deliveriesForMonth;
//	private int warm;
//	private int hot;
//	private int cold;
//	private int lostEnquiries;
//	private int droppedEnquiries;
//	private int totalCurrentEnquiries;
//	private int currentWarm;
//	private int currentHot;
//	private int currentCold;
//	private String cluster;
//	private String territoryManager;
	  private String state;
	   private BigInteger stateId;
	    private int openingHot;
	    private int openingWarm;
	    private int openingCold;
	    private int openingOverdue;	    
	    private int totalOpeningEnq;
	    private int currentHotEnq;
	    private int currentWarmEnq;
	    private int currentColdEnq;
	    private int currentOverdueEnq;
	    private int totalCurrentEnq;	
	
}
