/**
 * 
 */
package com.hitech.dms.web.model.inv.customer.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class CustDtlForInvResponseModel {
	private BigInteger customerId;
	private String customerType;
	private String customerCode;
	private String contactTitle;
	private String customerName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNo;
	private String organizationName;
	private String alternateNo;
	private String whatsAppNo;
	private String dateOfBirth;
	private String anniversaryDate;
	private String phoneNo;
	private String emailId;
	private String address1;
	private String address2;
	private String address3;
	private BigInteger pinId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private String gstIN;
	private String panNo;
}
