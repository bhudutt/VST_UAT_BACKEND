package com.hitech.dms.web.spare.party.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerCodeSearchResponse {
	
	private BigInteger parent_dealer_id;
	private String distributorCode;
	private String dealer;

}
