package com.hitech.dms.web.model.oldchassis.validate.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OldChassisValidateRequestModel {

	private BigInteger vinId;
	private String chassisNo;
	private String engineNo;
	@JsonProperty(value="vinNo")
	private String vinNo;
	private String registrationNo;
	private Date saledate;
	private Integer pcId;
	private Integer seriesId;
	private Integer segmentId; 
	private Integer modelId;
	private Integer variantId;
	private Integer itemId;
	private String status;
	private Integer id;
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
	private String status1;
	private Integer districtId;
	private String district;
	private Integer tehsilId;
	private String tehsil;
	private Integer cityId;
	private String city;
	private Integer pinId;
	private String pinCode;
	private Integer stateId;
	private String state;
	private Integer countryId;
	private String country;
	private Integer cid;
	private String plantCode;
	
}
