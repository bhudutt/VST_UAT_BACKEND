package com.hitech.dms.web.model.productTrial.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialEnqProspectResponseModel {
	private String enquiryNumber;
	private BigInteger enquiryId;
	private String enquiryStatus;
	private String enquiryDate;
	private BigInteger enqSourceId;
	private String sourceOfEnquiry;
	private BigInteger customerCategoryId;
	private String prospectCategory;
	private String enquiryStage;
	private BigInteger enquiryStageId;
	private String mobileNo;
	private String prospectCode;
	private String companyName;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String whatsappNo;
	private String alternateNo;
	private String phoneNumber;
	private String emailid;
	private String address1;
	private String address2;
	private String address3;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private BigInteger pinId;
	private BigInteger cityId;
	private BigInteger tehsilId;
	private BigInteger districtId;
	private BigInteger stateId;
	private BigInteger countryId;
	
	
	private BigInteger modelId;
	private String modelName;
	private String modelDesc;
	private String seriseName;
	private String segmentName; 
	private String variant; 
	private Integer pcId;
	private String profitCenter; 

}
