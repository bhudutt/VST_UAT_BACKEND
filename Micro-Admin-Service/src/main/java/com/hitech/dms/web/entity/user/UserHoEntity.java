/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_HO_USER")
@Data
public class UserHoEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4478968288111971967L;

	@Id
	@Column(name = "ho_usr_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger hoUserId;
	
	@Column(name = "BaseLocation")
	private String baseLocation;

	@Column(name = "PanNo")
	private String panNo;

	@Column(name = "FatherName")
	private String fatherName;

	@Column(name = "Qualification")
	private Integer qualification;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "DOJ")
	private Date doj;

	@Column(name = "Prof_Exp")
	private Float prof_Exp;

	@Column(name = "Remarks")
	private String remarks;

	@Column(name = "employee_code", updatable = false)
	private String employeeCode;

	@Column(name = "employee_name")
	private String employeeName;
	
	@Column(name = "p_first_name")
	private transient String pFirstName;
	
	@Column(name = "p_middle_name")
	private transient String pMiddleName;
	
	@Column(name = "p_last_name")
	private transient String pLastName;

	@Column(name = "Emp_ContactNo")
	private String empContactNo;

	@Column(name = "Emp_Mail")
	private String empMail;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "ho_designation_id")
	private Integer hoDesignationId;
	
	@Column(name = "ho_designation_level_id")
	private Integer hoDesignationLevelId;
	
	@Column(name = "reporting_user_id")
	private BigInteger reporting_user_id;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;

	@Column(name = "Pin_Id")
	private transient Integer pinId;
	
	@JsonIgnore
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@JsonIgnore
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@JsonIgnore
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@JsonIgnore
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "userForId")
	private Integer userForId;
	
	@Column(name = "platformId")
	private Integer platformId;
	
}
