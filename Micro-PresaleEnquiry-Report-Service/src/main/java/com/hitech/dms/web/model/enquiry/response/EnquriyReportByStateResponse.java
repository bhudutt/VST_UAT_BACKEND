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
public class EnquriyReportByStateResponse {
	private String state;
	private BigInteger stateId;
	private String dealerName;
	private BigInteger branchId;
	private BigInteger parentDealerId;
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
