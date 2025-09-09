/**
 * 
 */
package com.hitech.dms.web.model.dc.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class CustDtlCreateRequestModel {
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
