package com.hitech.dms.web.model.outletCategory.search.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyMasterApproveEntity {

	
	private BigInteger partyBranchId;
	private String status;
	private BigInteger hoUserId;
	private String remarks;
	
	
}

