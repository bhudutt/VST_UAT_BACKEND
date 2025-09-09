/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimCreateResponseModel {
	private BigInteger activityClaimHdrId;
	private String activityClaimNumber;
	private String msg;
	private int statusCode;

}
