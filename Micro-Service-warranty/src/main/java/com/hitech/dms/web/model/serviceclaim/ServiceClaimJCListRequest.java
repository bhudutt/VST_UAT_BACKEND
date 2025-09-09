package com.hitech.dms.web.model.serviceclaim;

import lombok.Data;

@Data
public class ServiceClaimJCListRequest {
	
	private String claimType;
	
	private String fromDate;
	
	private String toDate;

}
