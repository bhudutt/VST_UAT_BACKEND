/**
 * 
 */
package com.hitech.dms.web.model.customer.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class CustomerDTLByCustIDResponseModel {
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
	private Date dateOfBirth;
	private Date anniversaryDate;
	private String gstIN;
	private String panNo;
	private Double landInAcres;

}
