package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyMappingResponse {

	private BigInteger id;
	private String msg;
	private int statusCode;

}
