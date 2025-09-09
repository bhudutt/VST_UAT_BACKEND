/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class CustomerHDRRequestModel {
	private String prospectType;
	@NotNull(message = "Customer Category Is Required")
	private BigInteger customerCategoryId;
	private String customerCode;
	private String contactTitle;
	@NotNull(message = "First Name Is Required")
	private String firstName;
	private String middleName;
	private String lastName;
	@NotNull(message = "Customer Mobile Is Required")
	private String mobileNo;
	private String organizationName;
	private String alternateNo;
	private String whatsAppNo;
	private String phoneNo;
	private String emailId;
	@NotNull(message = "Address1 Is Required")
	private String address1;
	private String address2;
	private String address3;
	@NotNull(message = "Pincode Is Required")
	private BigInteger pinId;
	private BigInteger districtId;
	private BigInteger tehsilId;
	private BigInteger cityId;
	private BigInteger stateId;
	private BigInteger countryId;
	@JsonDeserialize(using = DateHandler.class)
	private Date dateOfBirth;
	@JsonDeserialize(using = DateHandler.class)
	private Date anniversaryDate;
	private String gstIN;
	private String panNo;
	private BigInteger occupationID;
	private Float landInAcres;
}
