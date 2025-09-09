/**
 * 
 */
package com.hitech.dms.web.model.party.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class FinancePatyByPartyCodeResponseModel {
	private BigInteger id;
	private String partyCode;
	private String partyName;
	private String partyLocation;
	private String partyAddress1;
	private String partyAddress2;
	private String partyAddress3;
	private String displayValue;
	private String mobileNumber;
	private String emailId;
	private BigInteger pinId;
	private String pincode;
	private String village;
	private String tehsil;
	private String city;
	private String district;
	private String state;
	private String country;
}
