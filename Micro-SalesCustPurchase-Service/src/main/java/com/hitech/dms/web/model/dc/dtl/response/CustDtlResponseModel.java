/**
 * 
 */
package com.hitech.dms.web.model.dc.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class CustDtlResponseModel {
	private BigInteger customerId;
	private String customerType;
	private String customerCode;
	private String contactTitle;
	private BigInteger customerCategoryId;
	private String customerName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNo;
	private String organizationName;
	private String alternateNo;
	private String whatsAppNo;
	private String phoneNo;
	private String emailId;
	private String dateOfBirth;
	private String anniversaryDate;
	private String address1;
	private String address2;
	private String address3;
	private String pincode;
	private BigInteger pinId;
	private String village;
	private String tehsil;
	private String city;
	private String district;
	private String state;
	private String country;
	private BigInteger cityId;
	private BigInteger districtId;
	private BigInteger tehsilId;
	private BigInteger stateId;
	private BigInteger countryId;
	private String gstIN;
	private String panNo;
}
