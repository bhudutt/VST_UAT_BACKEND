package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerCodeSearchResponse {
	
	private BigInteger parent_dealer_id;
	private String distributorCode;
	private String dealer;
	private String parentDealerName;
	private String parentDealerLocation;
	private String dealerPincode;
}
