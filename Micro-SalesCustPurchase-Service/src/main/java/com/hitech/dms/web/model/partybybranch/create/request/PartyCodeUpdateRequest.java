package com.hitech.dms.web.model.partybybranch.create.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCodeUpdateRequest {
	
	
	private String partyCode;
	private Integer partyBranchId;
	private String mobileNumber;
	private String aadharNumber;
	private String panNo;
	private String address1;
	private String address2;
	private String address3;
	private String email;
	private Integer branchId;
	private String gst;
	private String partyLocation;
	private BigInteger partyCategoryId;

	

}
