/**
 * 
 */
package com.hitech.dms.web.model.user;

import java.math.BigDecimal;
import java.util.Date;

import com.hitech.dms.web.entity.user.UserDealerEntity;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserDealerModel {
private Long userDLRId;
	
	private String userType;
	private Long branchId;
	private String empCode;
	private Boolean empStatus;
	private Boolean isDefaultSalesman;
	private Integer title;
	private String empName;
	private String empMiddleName;
	private String empLastName;
	private String empFullName;
	private String empMobile;
	private String empEmail;
	private Date birthDate;
	private String maritalStatus;
	private String gender;
	private String bloodGroup;
	private Date anniversaryDate;
	private String emergencyContactName;
	private String emergencyContactNumber;
	private Date joiningDate;
	private Date confirmationDate;
	private Date leavingDate;
	private BigDecimal salary;
	private String bankAcountNumber;
	private String bankName;
	private String bankBranch;
	private String pfNumber;
	private String esINumber;
	private String panNumber;
	private String qualification1;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
