package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartyCategoryResponse {

	private BigInteger partyCategoryId;
	private String partyCategoryName;
	private String partyCategoryCode;
}
