/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "adm_user_dealer", uniqueConstraints = { @UniqueConstraint(columnNames = "user_dlr_Id") })
@Data
public class UserDealerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3503278815907455781L;

	@Id
	@Column(name = "user_dlr_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userDLRId;
	
	@Column(name = "user_type", nullable = false)
	private String userType;
	
	@Column(name = "branch_Id")
	private Long branchId;
	
	@Column(name = "EmpCode")
	private String empCode;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean empStatus;
	
	@Column(name = "IsDefultSalesman")
	@Type(type = "yes_no")
	private Boolean isDefaultSalesman;
	
	@Column(name = "ContactTitle")
	private Integer title;

	@Column(name = "FirstName")
	private String empName;
	
	@Column(name = "MiddleName")
	private String empMiddleName;

	@Column(name = "LastName")
	private String empLastName;

	@Column(name = "EmpName")
	private String empFullName;

	@Column(name = "MobileNumber")
	private String empMobile;

	@Column(name = "Email")
	private String empEmail;
	
	@Column(name = "BirthDate")
	private Date birthDate;

	@Column(name = "MaritalStatus")
	private String maritalStatus;

	@Column(name = "Gender")
	private String gender;

	
	@Column(name = "BloodGroup")
	private String bloodGroup;
	
	@Column(name = "AnnivarsaryDate")
	private Date anniversaryDate;
	
	@Column(name = "EmgContactName")
	private String emergencyContactName;

	@Column(name = "EmgContactNo")
	private String emergencyContactNumber;

	@Column(name = "JoiningDate")
	private Date joiningDate;

	@Column(name = "ConfirmationDate")
	private Date confirmationDate;

	@Column(name = "LeavingDate")
	private Date leavingDate;

	@Column(name = "Salary")
	private BigDecimal salary;

	@Column(name = "BankAcNo")
	private String bankAcountNumber;

	@Column(name = "BankName")
	private String bankName;

	@Column(name = "BankBranch")
	private String bankBranch;

	@Column(name = "PFNo")
	private String pfNumber;

	@Column(name = "ESINo")
	private String esINumber;

	@Column(name = "PANNo")
	private String panNumber;
	
	@Column(name = "Qualification1")
	private String qualification1;
	
	@Column(name = "CreatedBy",updatable=false)
	private String createdBy;

	@Column(name = "CreatedDate",updatable=false)
	private Date createdDate;
	
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
