package com.hitech.dms.web.model.activityplan.dtl.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanDTLRequestModel {
	private BigInteger planActivityId;
	private String activityPlanNumber;
	private String isFor;
}
