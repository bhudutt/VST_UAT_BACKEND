/**
 * 
 */
package com.hitech.dms.web.model.allot.create.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachineAllotCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	private BigInteger dealerId;
	@JsonProperty(value = "branchId", required = true)
	private BigInteger branchId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	//@JsonDeserialize(using = DateHandler.class)
	private Date allotDate;
	@JsonProperty(value = "onlyImplementFlag", required = true)
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
	private String state;
	private String country;
	@JsonDeserialize(using = DateHandler.class)
	private Date dateOfBirth;
	@JsonDeserialize(using = DateHandler.class)
	private Date anniversaryDate;
	private String gstIN;
	private String panNo;
	private BigInteger occupationID;
	private Float landInAcres;
	private String productGroup;
	private String businessType;

	private List<MachineAllotDtlCreateRequestModel> enqMachineDtlList;
	private List<MachineAllotItemDtlCreateRequestModel> enqItemDtlList;
}
