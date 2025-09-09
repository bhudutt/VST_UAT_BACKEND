package com.hitech.dms.web.model.updateoldchassis;

import java.math.BigInteger;

import lombok.Data;

@Data
public class VinAndCustDtlResponse {

	private BigInteger vinId;
	
	private Integer pcId;
	
	private String pcDesc;
	
	private String chassisNo;
	
	private String engineNo;
	
	private String vinNo;
	
	private String registrationNo;
	
	private String saleDate;
	
	private BigInteger modelId;
	
	private String modelName;
	
	private String seriesName;
	
	private String segmentName;
	
	private String itemNo;
	
	private BigInteger itemId;
	
	private String itemDesc;
	
	private String variant;
	
	private BigInteger customerId;
	
	private String mobileNo;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String whatsappNo;
	
	private String alternateNo;
	
	private String emailId;
	
	private String panNo;
	
	private String aadharNo;
	
	private String address1;
	
	private String address2;
	
	private String address3;
	
	private String district;
	
	private String tehsil;
	
	private String city;
	
	private String pinName;
	
	private String state;
	
	private String country;
	
	private BigInteger districtId;
	
	private BigInteger tehsilId;
	
	private BigInteger cityId;
	
	private BigInteger pinId;
	
	private BigInteger stateId;
	
	private BigInteger countryId;
}
