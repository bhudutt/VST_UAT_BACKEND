package com.hitech.dms.web.spare.party.model.mapping.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class PartyMappingRequest {

	private Integer partyCategoryId;
	private Integer partyBranchId;
	private List<DistributorModel> distributorList;

}
