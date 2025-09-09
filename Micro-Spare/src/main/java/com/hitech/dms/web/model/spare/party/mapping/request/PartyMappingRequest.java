package com.hitech.dms.web.model.spare.party.mapping.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.party.mapping.response.DistributorModel;

import lombok.Data;

@Data
public class PartyMappingRequest {

	private Integer partyCategoryId;
	private Integer partyBranchId;
	private List<DistributorModel> distributorList;

}
