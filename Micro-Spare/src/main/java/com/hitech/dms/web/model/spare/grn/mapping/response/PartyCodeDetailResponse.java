package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCodeDetailResponse {
	
	private BigInteger partyCategoryId;
	private String PartyName;
	private String PartyCode;
	private BigInteger branchId;
	private BigInteger partyBranchId;

}
