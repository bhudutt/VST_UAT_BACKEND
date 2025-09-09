package com.hitech.dms.web.model.customer.create;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CustServiceSaveReq {

		private String customerName;
		private Integer customerCtgId;
		private String contactTitle;
		private String firstName;
		private String middleName;
		private String lastName;
		private String organizationName;
		private String emailId;
		private String mobileNo;
		private String alternateNo;
		private String whatsappNo;
		private String phoneNumber;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private Date anniversaryDate;
		private String gstIn;
		private String dlNumber;
		private float landInAcres;
		private String address1;
		private String address2;
		private String address3;
		private Integer countryId;
		private Integer stateId;
		private BigInteger pinCode;
		private String fatherName;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private Date dateOfBirth;
		private String panNo;
		private String aadharCardNo;
		private String annualIncome;
		private String qualification;
		private String gender;
		private String isMarried;
		private Integer distinctId;
		private Integer tehsilId;
		private Integer cityId;
	
}