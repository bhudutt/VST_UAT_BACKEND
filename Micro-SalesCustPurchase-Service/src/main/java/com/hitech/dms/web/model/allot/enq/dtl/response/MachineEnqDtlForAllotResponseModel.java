/**
 * 
 */
package com.hitech.dms.web.model.allot.enq.dtl.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineEnqDtlForAllotResponseModel {
	private String msg;
	private BigInteger customerId;
	private String customerType;
	private BigInteger customerCategoryId;
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
	private BigInteger districtId;
	private BigInteger tehsilId;
	private BigInteger cityId;
	private BigInteger stateId;
	private BigInteger countryId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private String dateOfBirth;
	private String anniversaryDate;
	private String gstIN;
	private String panNo;
	private BigInteger occupationID;
	private Float landInAcres;
	private Integer quantity;
	private List<MachineEnqMachDtlForAllotResponseModel> enqMachineDtlList;
	private String salesMan;
}
