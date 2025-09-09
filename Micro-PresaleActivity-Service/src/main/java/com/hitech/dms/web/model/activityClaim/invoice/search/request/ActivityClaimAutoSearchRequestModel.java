package com.hitech.dms.web.model.activityClaim.invoice.search.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ActivityClaimAutoSearchRequestModel {
	private BigInteger dealerId;
	private String searchText;
	private String isFor;
}
