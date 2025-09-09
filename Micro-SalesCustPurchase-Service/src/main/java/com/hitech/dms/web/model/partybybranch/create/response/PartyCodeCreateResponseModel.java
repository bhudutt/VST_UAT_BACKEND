package com.hitech.dms.web.model.partybybranch.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCodeCreateResponseModel {
	private BigInteger partyCodeId;
	private String partyNumber;
	private String msg;
	private Integer statusCode;
}
