package com.hitech.dms.web.model.spare.claim.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.inventory.request.PartDetailRequest;

import lombok.Data;

@Data
public class SpareClaimSearchRequest {

	private String claimNumber;
	private String claimType;
	private String fromDate; 
	private String toDate;
	private String userCode; 
	private Integer page;
	private Integer size;
	private Integer pcId;
	private Integer hoId;
	private Integer zoneId;
	private Integer stateId;
	private Integer territoryId;
}
