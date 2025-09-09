package com.hitech.dms.web.model.activityClaim.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class ActivityClaimCreateResponseModel {
	private BigInteger activityClaimHdrId;
	private String activityClaimNumber;
	private String msg;
	private int statusCode;

}
