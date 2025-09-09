/**
 * 
 */
package com.hitech.dms.web.model.allot.view.response;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineAllotViewResponseModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;
	private String action;
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String allotDate;
	private Date allotDate1;
	private String allotStatus;
	private String deAllotBy;
	private String deAllotDate;
	private String deAllotReason;
	private String enquiryDate;
	private boolean onlyImplementFlag;
	private BigInteger customerId;
	private BigInteger enquiryId;
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
	private String city;
	private String state;
	private String country;
	private String dateOfBirth;
	private String anniversaryDate;
	private String gstIN;
	private String panNo;
	private BigInteger occupationID;
	private Float landInAcres;
	private String enquiryNumber;
	
	private List<MachineAllotMachDtlViewResponseModel> machineAllotDtlList;
	private String salesmanName;
}
