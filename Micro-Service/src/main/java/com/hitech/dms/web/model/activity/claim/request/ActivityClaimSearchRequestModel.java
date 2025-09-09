/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String activityClaimNumber;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;
}
