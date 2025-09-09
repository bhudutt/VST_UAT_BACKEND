package com.hitech.dms.web.entity.dealer.employee.create;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Table(name = "ADM_BP_DEALER_EMP")
@Entity
@Data
public class DealerEmployeeEntity implements Serializable, Cloneable {
	/**
	* 
	*/
	private static final long serialVersionUID = 5278962616224370730L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_Id")
	private BigInteger employeeId;

	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer is Required")
	@Column(name = "dealer_id")
	private BigInteger dealerId;

	@JsonProperty(value = "isDefultSalesman", required = true)
	@NotNull(message = "Salesman is Required")
	@Column(name = "IsDefultSalesman")
	private char isDefultSalesman;

	@JsonProperty(value = "employeeCode", required = true)
	@NotNull(message = "Employee Code is Required")
	@Column(name = "EmpCode")
	private String employeeCode;

	@JsonProperty(value = "isActive", required = true)
	@NotNull(message = "IsActive is Required")
	@Column(name = "IsActive")
	private char isActive;

	@JsonProperty(value = "dealerDepartmentId", required = true)
	@NotNull(message = "Dealer Department is Required")
	@Column(name = "dlr_department_Id")
	private Integer dealerDepartmentId;

	@JsonProperty(value = "dealerDesignationId", required = true)
	@NotNull(message = "Dealer Designation is Required")
	@Column(name = "dlr_designation_Id")
	private Integer dealerDesignationId;

	@Column(name = "ContactTitle")
	private BigInteger titleId;
	private transient String title;

	@JsonProperty(value = "firstName", required = true)
	@NotNull(message = "First Name is Required")
	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "MiddleName")
	private String middleName;

	@JsonProperty(value = "lastName", required = true)
	@NotNull(message = "Last Name is Required")
	@Column(name = "LastName")
	private String lastName;

	@Column(name = "MobileNumber")
	private String mobileNumber;

	@Column(name = "Email")
	private String email;

	@Column(name = "AlternateMobileNo")
	private String alternateMobileNo;

	@Column(name = "Driving_Licence_Type")
	private BigInteger drivingLicenceTypeId;
	private transient String drivingLicenceType;

	@Column(name = "Driving_Licence_No")
	private String drivingLicenceNo;

	@Column(name = "Driving_Licence_ExpiryDate")
	private Date drivingLicenceExpiryDate;

	@Column(name = "reporting_employee_id")
	private BigInteger reportingRmployeeId;

	@Column(name = "BirthDate")
	private Date birthDate;

	@Column(name = "AnnivarsaryDate")
	private Date annivarsaryDate;

	@Column(name = "Qualification1")
	private String qualification;

	@Column(name = "MaritalStatus")
	private String maritalStatus;

	@Column(name = "BloodGroup")
	private String bloodGroup;

	@JsonProperty(value = "gender", required = true)
	@NotNull(message = "Gender is Required")
	@Column(name = "Gender")
	private String gender;

	@Column(name = "EmgContactName")
	private String emgContactName;

	@Column(name = "EmgContactNo")
	private String emgContactNo;

	@Column(name = "JoiningDate")
	private Date joiningDate;

	@Column(name = "Salary")
	private BigDecimal salary;

	@Column(name = "LeavingDate")
	private Date leavingDate;

	@Column(name = "PFNo")
	private String pfNo;

	@Column(name = "PANNo")
	private String panNo;

	@Column(name = "ESINo")
	private String esiNo;

	@Column(name = "BankAcNo")
	private String bankAcNo;

	@Column(name = "BankName")
	private String bankName;

	@Column(name = "BankBranch")
	private String bankBranch;
	
	@Column(name = "ifscCode")
	private String ifscCode;
	
	@Column(name = "passpostNo")
	private String passpostNo;
	
	@Column(name = "adharcardNo")
	private String adharcardNo;
	
	

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dealerEmpHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<DealerEmployeeAddressEntity> dealerAddress;

	public DealerEmployeeEntity() {

	}

	public DealerEmployeeEntity(DealerEmployeeEntity dealerEmployee) {
		super();
		this.dealerId = dealerEmployee.getDealerId();
		this.isDefultSalesman = dealerEmployee.getIsDefultSalesman();
		this.employeeCode = dealerEmployee.getEmployeeCode();
		this.isActive = dealerEmployee.getIsActive();
		this.dealerDepartmentId = dealerEmployee.getDealerDepartmentId();
		this.dealerDesignationId = dealerEmployee.getDealerDesignationId();
		this.titleId = dealerEmployee.getTitleId();
		this.title = dealerEmployee.getTitle();
		this.firstName = dealerEmployee.getFirstName();
		this.middleName = dealerEmployee.getMiddleName();
		this.lastName = dealerEmployee.getLastName();
		this.mobileNumber = dealerEmployee.getMobileNumber();
		this.email = dealerEmployee.getEmail();
		this.alternateMobileNo = dealerEmployee.getAlternateMobileNo();
		this.drivingLicenceTypeId = dealerEmployee.getDrivingLicenceTypeId();
		this.drivingLicenceType = dealerEmployee.getDrivingLicenceType();
		this.drivingLicenceNo = dealerEmployee.getDrivingLicenceNo();
		this.drivingLicenceExpiryDate = dealerEmployee.getDrivingLicenceExpiryDate();
		this.reportingRmployeeId = dealerEmployee.getReportingRmployeeId();
		this.birthDate = dealerEmployee.getBirthDate();
		this.annivarsaryDate = dealerEmployee.getAnnivarsaryDate();
		this.qualification = dealerEmployee.getQualification();
		this.maritalStatus = dealerEmployee.getMaritalStatus();
		this.bloodGroup = dealerEmployee.getBloodGroup();
		this.gender = dealerEmployee.getGender();
		this.emgContactName = dealerEmployee.getEmgContactName();
		this.emgContactNo = dealerEmployee.getEmgContactNo();
		this.joiningDate = dealerEmployee.getJoiningDate();
		this.salary = dealerEmployee.getSalary();
		this.leavingDate = dealerEmployee.getLeavingDate();
		this.pfNo = dealerEmployee.getPfNo();
		this.panNo = dealerEmployee.getPanNo();
		this.esiNo = dealerEmployee.getEsiNo();
		this.bankAcNo = dealerEmployee.getBankAcNo();
		this.bankName = dealerEmployee.getBankName();
		this.bankBranch = dealerEmployee.getBankBranch();
		this.dealerAddress = dealerEmployee.getDealerAddress();
		this.adharcardNo=dealerEmployee.getAdharcardNo();
		this.ifscCode=dealerEmployee.getIfscCode();
		this.passpostNo=dealerEmployee.getPasspostNo();

	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		DealerEmployeeEntity employeeMasterEntity = null;
		try {
			employeeMasterEntity = (DealerEmployeeEntity) super.clone();
	    } catch (CloneNotSupportedException e) {
	    	employeeMasterEntity = new DealerEmployeeEntity(
	          this.getEmployeeId(), 
	  		this.getDealerId(),
	  		this.getIsDefultSalesman(),
	  		this.getEmployeeCode(),
	  		this.getIsActive(),
	  		this.getDealerDepartmentId(),
	  		this.getDealerDesignationId(),
	  		this.getTitleId(),
	  		this.getTitle(),
	  		this.getFirstName(),
	  		this.getMiddleName(),
	  		this.getLastName(),
	  		this.getMobileNumber(),
	  		this.getEmail(),
	  		this.getAlternateMobileNo(),
	  		this.getDrivingLicenceTypeId(),
	  		this.getDrivingLicenceType(),
	  		this.getDrivingLicenceNo(),
	  		this.getDrivingLicenceExpiryDate(),
	  		this.getReportingRmployeeId(),
	  		this.getBirthDate(),
	  		this.getAnnivarsaryDate(),
	  		this.getQualification(),
	  		this.getMaritalStatus(),
	  		this.getBloodGroup(),
	  		this.getGender(),
	  		this.getEmgContactName(),
	  		this.getEmgContactNo(),
	  		this.getJoiningDate(),
	  		this.getSalary(),
	  		this.getLeavingDate(),
	  		this.getPfNo(),
	  		this.getPanNo(),
	  		this.getEsiNo(),
	  		this.getBankAcNo(),
	  		this.getBankName(),
	  		this.getBankBranch(),
	  		this.getCreatedBy(),
	  		this.getCreatedDate(),
	  		this.getModifiedBy(),
	  		this.getModifiedDate(),
	  		this.getDealerAddress());
	    }
		if(this.getDealerAddress() != null) {
			employeeMasterEntity.dealerAddress = this.dealerAddress.stream().map(DealerEmployeeAddressEntity::new).collect(Collectors.toList());			
		}
		return employeeMasterEntity;
	}

	public DealerEmployeeEntity(BigInteger employeeId, @NotNull(message = "Dealer is Required") BigInteger dealerId,
			@NotNull(message = "Salesman is Required") char isDefultSalesman,
			@NotNull(message = "Employee Code is Required") String employeeCode,
			@NotNull(message = "IsActive is Required") char isActive,
			@NotNull(message = "Dealer Department is Required") Integer dealerDepartmentId,
			@NotNull(message = "Dealer Designation is Required") Integer dealerDesignationId, BigInteger titleId,
			String title, @NotNull(message = "First Name is Required") String firstName, String middleName,
			@NotNull(message = "Last Name is Required") String lastName, String mobileNumber, String email,
			String alternateMobileNo, BigInteger drivingLicenceTypeId, String drivingLicenceType,
			String drivingLicenceNo, Date drivingLicenceExpiryDate, BigInteger reportingRmployeeId, Date birthDate,
			Date annivarsaryDate, String qualification, String maritalStatus, String bloodGroup,
			@NotNull(message = "Gender is Required") String gender, String emgContactName, String emgContactNo,
			Date joiningDate, BigDecimal salary, Date leavingDate, String pfNo, String panNo, String esiNo,
			String bankAcNo, String bankName, String bankBranch, String createdBy, Date createdDate,
			String modifiedBy, Date modifiedDate, List<DealerEmployeeAddressEntity> dealerAddress) {
		super();
		this.employeeId = employeeId;
		this.dealerId = dealerId;
		this.isDefultSalesman = isDefultSalesman;
		this.employeeCode = employeeCode;
		this.isActive = isActive;
		this.dealerDepartmentId = dealerDepartmentId;
		this.dealerDesignationId = dealerDesignationId;
		this.titleId = titleId;
		this.title = title;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.alternateMobileNo = alternateMobileNo;
		this.drivingLicenceTypeId = drivingLicenceTypeId;
		this.drivingLicenceType = drivingLicenceType;
		this.drivingLicenceNo = drivingLicenceNo;
		this.drivingLicenceExpiryDate = drivingLicenceExpiryDate;
		this.reportingRmployeeId = reportingRmployeeId;
		this.birthDate = birthDate;
		this.annivarsaryDate = annivarsaryDate;
		this.qualification = qualification;
		this.maritalStatus = maritalStatus;
		this.bloodGroup = bloodGroup;
		this.gender = gender;
		this.emgContactName = emgContactName;
		this.emgContactNo = emgContactNo;
		this.joiningDate = joiningDate;
		this.salary = salary;
		this.leavingDate = leavingDate;
		this.pfNo = pfNo;
		this.panNo = panNo;
		this.esiNo = esiNo;
		this.bankAcNo = bankAcNo;
		this.bankName = bankName;
		this.bankBranch = bankBranch;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.dealerAddress = dealerAddress;
	}
	
	

}
