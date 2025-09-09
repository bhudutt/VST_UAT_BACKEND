package com.hitech.dms.web.model.oldchassis.validate.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OldChassisVinDetailsResponseModel {
	
	private BigInteger vinId;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String registrationNo;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date saledate;
	private Integer pcId;
	private String pcName;
	private Integer seriesId;
	private String seriesName;
	private Integer segementId;
	private String segementName;
	private Integer modelId;
	private String modelName;
	private Integer variantId;
	private String variant;
	private String item;
	private Integer itemId;
	private String itemDescription;
	private String status;
	private BigInteger customerId;
	private String mobileNo;
	private String firstname;
	private String middlename;
	private String lastname;
	private String whatappNo;
	private String alternateNo;
	private String emailId;
	private String panNo;
	private String aadharNo;
	private String address1;
	private String address2;
	private String address3;
	private String status1;
	private Integer cid;
	private Integer districtId;
	private String district;
	private Integer tehsilId;
	private String tehsil;
	private Integer cityId;
	private String city;
	private BigInteger pinId;
	private Integer pinCode;
	private Integer stateId;
	private String state;
	private Integer countryId;
	private String country;
	private String plantCode;
}
