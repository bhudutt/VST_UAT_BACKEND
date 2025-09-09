package com.hitech.dms.web.model.customer.create;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CustomerDetailsResponse {

		private BigInteger customerId;
		private String customerCode;
		private BigInteger customerCategoryID;
		private String customerCategory;
		private String contactTitle;
		private String firstName;
		private String middleName;
		private String lastName;
		private String mobileNumber;
		private String alternateNumber;
		private String watsupNumber;
		private String emailId;
		private String address1;
		private String address2;
		private String address3;
		private BigInteger tehsilId;
		private String tehsilDesc;
		private BigInteger districtId;
		private String districtDesc;
		private BigInteger pinID;
		private String pinCode;
		private BigInteger cityID;
		private String cityDesc;
		private BigInteger countryID;
		private String countryDesc;
		private BigInteger stateID;
		private String stateDesc;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date dateOfBirth;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date anniversaryDate;
		private String gstIN;
		private String panNo;
		private Double landInAcres;
		private String prospectType;
		private String gender;
		private String fatherName;
		private String aadharCardNo;
		private String annualIncome;
		private String isMarried;
		private String qualification;
		private String organizationName;
		private String phoneNumber;
		private String dlNo;
		
		
		
		
	

}
