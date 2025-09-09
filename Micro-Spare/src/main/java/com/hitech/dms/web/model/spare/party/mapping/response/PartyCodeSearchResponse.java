package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCodeSearchResponse {

	private String partyCode;
	private BigInteger partyBranchId;
	private BigInteger partyCategoryId;
	private String partyName;
	private String partyLocation;
	private String pinCode;
	

}
