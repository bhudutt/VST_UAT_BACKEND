/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.productTrial.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialEnquiryHistoryResponse {
	
	private String enquiryNumber;
	private String ProductTrailNo;
	private String ProductTrailDate;
	private String modelName;
	private String chassisNo;
	private BigDecimal overallRating;
	private String remarks;
	private String trailGivenBy;

}
