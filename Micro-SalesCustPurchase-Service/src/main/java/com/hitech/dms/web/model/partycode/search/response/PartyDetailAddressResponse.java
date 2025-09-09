package com.hitech.dms.web.model.partycode.search.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyDetailAddressResponse {

	
	private BigInteger pinID;
	private String pinCode;
	private String localityCode;
	private String localityName;
	private String displayName;
	private BigInteger cityID;
	private String cityDesc;
	private BigInteger tehsilID;
	private String tehsilDesc;
	private BigInteger districtID;
	private String districDesc;
	private BigInteger stateID;
	private String stateDesc;
	private BigInteger countryID;
	private String countryDesc;
	private String statusMessage;
	private int statusCode;

}
