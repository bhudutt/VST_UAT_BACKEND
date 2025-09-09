package com.hitech.dms.web.model.spare.party.mapping.response;

import lombok.Data;

@Data
public class PartyDistributorMappingResponse {

	private int id;
	private String distributorCode;
	private String distributorName;
	private String pinCode;
	private String district;
	private String tehsil;
	private String state;


}
