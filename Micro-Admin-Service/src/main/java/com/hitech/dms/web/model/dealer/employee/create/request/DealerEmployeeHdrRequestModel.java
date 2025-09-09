package com.hitech.dms.web.model.dealer.employee.create.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerEmployeeHdrRequestModel {
	
//	@JsonProperty(value = "employeeId", required = true)
//	@NotNull(message = "Employee is Required")
	private BigInteger employeeId;
	
	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer is Required")
	private BigInteger dealerId;
	
	
	@JsonProperty(value = "isDefultSalesman", required = true)
	@NotNull(message = "Salesman is Required")
	private char isDefultSalesman;
	
	
//	@JsonProperty(value = "employeeCode", required = true)
//	@NotNull(message = "Employee Code is Required")
	private String employeeCode;
	
	@JsonProperty(value = "isActive", required = true)
	@NotNull(message = "IsActive is Required")
	private char isActive;
	
	
	@JsonProperty(value = "dealerDepartmentId", required = true)
	@NotNull(message = "Dealer Department is Required")
	private Integer dealerDepartmentId;
	
	@JsonProperty(value = "dealerDesignationId", required = true)
	@NotNull(message = "Dealer Designation is Required")
	private Integer dealerDesignationId;

	
	private String title;
	private BigInteger titleId;
	
	@JsonProperty(value = "firstName", required = true)
	@NotNull(message = "First Name is Required")
	private String firstName;
	
	private String middleName;
	
	@JsonProperty(value = "lastName", required = true)
	@NotNull(message = "Last Name is Required")
	private String lastName;
	
	private String mobileNumber;
	
	private String email;
	
	private String alternateMobileNo;
	
	
	private String drivingLicenceType;
	private BigInteger drivingLicenceTypeId;

	private String drivingLicenceNo;

	@JsonDeserialize(using = DateHandler.class)
	private Date drivingLicenceExpiryDate;
	

	private BigInteger reportingRmployeeId;
	
	@JsonDeserialize(using = DateHandler.class)
	private Date birthDate;
	
	@JsonDeserialize(using = DateHandler.class)
	private Date annivarsaryDate;

	private String qualification;

	private String maritalStatus;

	private String bloodGroup;
	
	@JsonProperty(value = "gender", required = true)
	@NotNull(message = "Gender is Required")
	private String gender;

	private String emgContactName;

	private String emgContactNo;
	
	@JsonDeserialize(using = DateHandler.class)
	private Date joiningDate;

	private Integer salary;

	@JsonDeserialize(using = DateHandler.class)
	private Date leavingDate;

	private String pfNo;

	private String panNo;

	private String esiNo;

	private String bankAcNo;

	private String bankName;

	private String bankBranch;
	private String ifscCode;
	private String passpostNo;
	private String adharcardNo;

	private List<DealerEmployeeAddressRequestModel> dealerAddress;

}
