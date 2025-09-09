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
public class ActivityPlanAutoResponseModel {
	private BigInteger activityPlanHdrId;
	private String activityPlanNo;
	private String activityCreationDate;

}
