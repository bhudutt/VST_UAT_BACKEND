package com.hitech.dms.web.model.activityClaim.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class FetchActivityPlanNoAutoRequestMode {
	private BigInteger dealerId;
	private String isFor;
	private String searchText;
}
