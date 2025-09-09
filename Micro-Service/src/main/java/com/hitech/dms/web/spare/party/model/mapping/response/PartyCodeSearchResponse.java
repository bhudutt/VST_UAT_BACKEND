package com.hitech.dms.web.spare.party.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCodeSearchResponse {

	private String partyCode;
	private BigInteger partyBranchId;
	private BigInteger partyCategoryId;

}
