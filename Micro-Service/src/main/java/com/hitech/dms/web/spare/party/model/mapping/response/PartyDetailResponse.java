package com.hitech.dms.web.spare.party.model.mapping.response;

import lombok.Data;

@Data
public class PartyDetailResponse {
	
	private String PartyName;
	private Character PartyStatus;
	private String PartyLocation;
	private String PartyMobileNo;
	private String PartyEmailId;
	private String GstNo;
	private String District;
	private String Tehsil;
	private String Village;
	private String Pincode;
	private String State;
	private String Country;
}
