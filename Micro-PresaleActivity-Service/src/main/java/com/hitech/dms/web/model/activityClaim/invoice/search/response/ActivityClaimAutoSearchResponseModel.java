/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ActivityClaimAutoSearchResponseModel {
	private BigInteger id;
	private String docNumber;
	private String dealerCode;
	private BigInteger dealerId;
}
