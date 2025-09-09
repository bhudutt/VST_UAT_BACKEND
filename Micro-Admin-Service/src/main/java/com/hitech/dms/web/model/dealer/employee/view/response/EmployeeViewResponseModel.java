/**
 * 
 */
package com.hitech.dms.web.model.dealer.employee.view.response;

import java.math.BigDecimal;
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
public class EmployeeViewResponseModel {
	private BigInteger dealerId; //
	private String dealerName;
	private BigInteger employeeId; //
	private char isDefultSalesman;
	private String employeeCode;
	private char isActive;
	private Integer dealerDepartmentId;
	private String dealerDepartment;
	private Integer dealerDesignationId;
	private String dealerDesignation;
	private String title;
	private BigInteger titleId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;

	private String email;

	private String alternateMobileNo;

	private String drivingLicenceType;
	private BigInteger drivingLicenceTypeId;

	private String drivingLicenceNo;

	private String drivingLicenceExpiryDate;

	private BigInteger reportingEmployeeId;
	private String reportingEmployee;
	private String birthDate;
	private String annivarsaryDate;

	private String qualification;

	private String maritalStatus;

	private String bloodGroup;
	private String gender;

	private String emgContactName;

	private String emgContactNo;
	private String joiningDate;

	private BigDecimal salary;

	private String leavingDate;

	private String pfNo;

	private String panNo;

	private String esiNo;

	private String bankAcNo;

	private String bankName;

	private String bankBranch;
	
	private String ifscCode;
	private String passpostNo;
	private String adharcardNo;

	private List<EmployeeAddressViewResponseModel> dealerAddressList;
}
