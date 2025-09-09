/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvRequestModel {
	private BigInteger id;
	private String isFor;
	private Integer dealerId;
	private String location;
}
