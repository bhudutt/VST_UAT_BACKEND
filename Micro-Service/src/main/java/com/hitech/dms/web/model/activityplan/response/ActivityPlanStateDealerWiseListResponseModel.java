package com.hitech.dms.web.model.activityplan.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ActivityPlanStateDealerWiseListResponseModel {
	
    private BigInteger branchId;
    private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
}
