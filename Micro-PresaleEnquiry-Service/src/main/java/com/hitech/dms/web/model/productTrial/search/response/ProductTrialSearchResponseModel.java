package com.hitech.dms.web.model.productTrial.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialSearchResponseModel {

	private BigInteger id; // productTrialId
	private String  productTrailNo;
	private String productTrailDate;
	private BigInteger enquiryId;
	private String enquiryNumber;
	private String trailGivenBy;
	private BigDecimal overallRating;
	private String startTime;
	private String endTime;
	private String startKm;
	private String endKm;
	private String tempRegNo;
	private String chassisNo;
	private String remarks;
	private BigInteger branchId;

}
