package com.hitech.dms.web.model.activity.claim.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ActivityClaim {
	
	private BigInteger activityId;
	
	private BigDecimal approvalBudget;

}
