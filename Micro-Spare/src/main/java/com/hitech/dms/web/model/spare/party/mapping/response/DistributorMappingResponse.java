package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DistributorMappingResponse {

	private Integer id;
	private String distributorCode;
	private String distributorName;
	private String pinCode;
	private String district;
	private String tehsil;
	private String state;


}
