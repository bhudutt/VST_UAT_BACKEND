package com.hitech.dms.web.model.activityClaim.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimHdrDtlRequestModel {
	private BigInteger dealerId;
	private BigInteger id;
	private String isFor;

}
