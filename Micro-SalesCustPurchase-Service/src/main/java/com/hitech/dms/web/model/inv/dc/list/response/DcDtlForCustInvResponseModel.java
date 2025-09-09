/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.list.response;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.inv.customer.response.CustDtlForInvResponseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcDtlForCustInvResponseModel {
	private BigInteger insuranceMasterId;
	private String partyCode;
	private String partyName;
	private String partyLocation;
	private String partyAddress1;
	private String partyAddress2;
	private String partyAddress3;
	private String mobileNumber;
	private String emailId;
	private String gstIN;
	private String panNo;
	private BigInteger pinId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private String policyStartDate;
	private String policyEndDate;
	
	private CustDtlForInvResponseModel customerDetail;
	private List<DcListForInvResponseModel> dcList;
}
