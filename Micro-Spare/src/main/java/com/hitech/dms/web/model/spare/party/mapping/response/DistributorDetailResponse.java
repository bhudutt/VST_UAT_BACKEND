package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;


import lombok.Data;

@Data
public class DistributorDetailResponse {

	private String distributorCode;
	private String distributorName;
	private String pinCode;
	private BigInteger pinId;
	private String district;
	private String tehsil;
	private String state;
	private String branchId;
	private int statusCode;
	private String msg;
	//
	//added by Vivek Gupta
	
	private String dealerAddress1;
	private String dealerAddress2;
	private String dealerAddress3;
	private String dealerCity;
	private String dealerCountry;
	private String dealerPincode;
	private String mobileNumber;
	private String gstNo;
	private String panNo;
	private String tanNo;
	private String cinNo;			
		

}
