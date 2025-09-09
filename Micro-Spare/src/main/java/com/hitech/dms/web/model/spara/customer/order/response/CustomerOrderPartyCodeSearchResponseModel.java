package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CustomerOrderPartyCodeSearchResponseModel {

	private BigInteger partyCategoryId;
	private String address;
	private String tehsilTalukaMandal;
	private BigInteger pinId;
	private String pincode;
	private String partyCode;
	private String state;
	private String cityVillage;
	private String partyName;
	private String district;
	private String postOffice;
	private String country;
	
	private String panNo;
	private String gstNo;
	private String address1;
	private String address2;
	private String address3;
	private String mobileNo;
	
	
	
	

}
