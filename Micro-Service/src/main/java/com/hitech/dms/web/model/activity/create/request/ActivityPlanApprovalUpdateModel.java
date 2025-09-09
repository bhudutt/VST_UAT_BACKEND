package com.hitech.dms.web.model.activity.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ActivityPlanApprovalUpdateModel {

	private BigInteger activityPlanStatus;
	private Integer activityPlanId;
	private BigDecimal totalActivityCost;
}
