/**
 * 
 */
package com.hitech.dms.web.model.user;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserCustomerModel {
	private Long usrCustId;
	private String email;
	private String userType;
	private String userCode;
	private String userName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String password;
	private String confirmPassword;
	private Boolean empStatus;
	private Boolean isEmailVerified;
	private String empMobile;
	private Date birthDate;
	private BigInteger maritalStatusId;
	private transient String maritalStatus;
	private BigInteger genderId;
	private String gender;
	private Date anniversaryDate;
	private String qualification1;
	private String panNumber;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
