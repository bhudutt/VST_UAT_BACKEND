
package com.hitech.dms.web.model.partycode.search.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerDetailResponse {
	
	
	private BigInteger parentDealerId;
	private String parentDealerCode;
	private String dealerAddress1;
	private String dealerAddress2;
	private String dealerAddress3;
	private String dealerCity;
	private String dealerDistrict;
	private String dealerTahsil;
	private String dealerState;
	private String dealerCountry;
	private String dealerPinCode;
	private String mobileNumber;
	private String dealerEmail;
	private String statusCode;
	private String message;
	
	

	

}
