/**
 * 
 */
package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author Singh.Singh
 *
 */
@Entity
@Table(name = "CM_CUST_HDR")
@Data
public class CustomerHDREntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 967041673311533038L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Customer_Id")
	private BigInteger customerId;
    
	@JsonProperty(value="prospectType")
	@Column(name = "ProspectType")
	private String prospectType;
    
	@JsonProperty(value="customerGroupId")
	@Column(name = "CustomerGroup_Id")
	private BigInteger customerGroupId;
    
	@JsonProperty(value="customerCategoryId")
	@Column(name = "CustomerCategory_Id")
	private BigInteger customerCategoryId;

	@JsonProperty(value="customerCode")
	@Column(name = "CustomerCode", updatable = false)
	private String customerCode;
    
	@JsonProperty(value="contactTitle")
	@Column(name = "ContactTitle")
	private String contactTitle;

	@JsonProperty(value="firstName")
	@Column(name = "FirstName")
	private String firstName;
    
	@JsonProperty(value="middleName")
	@Column(name = "MiddleName")
	private String middleName;

	@JsonProperty(value="lastName")
	@Column(name = "LastName")
	private String lastName;

	@JsonProperty(value="mobileNo")
	@Column(name = "Mobile_No")
	private String mobileNo;
    
	@JsonProperty(value="alternateNo")
	@Column(name = "Alternate_No")
	private String alternateNo;
    
	@JsonProperty(value="whatsAppNo")
	@Column(name = "WhatsappNo")
	private String whatsAppNo;
	
	@JsonProperty(value="phoneNo")
	@Column(name = "PhoneNumber")
	private String phoneNo;
    
	@JsonProperty(value="organizationName")
	@Column(name = "Organization_Name")
	private String organizationName;
    
	@JsonProperty(value="emailId")
	@Column(name = "Email_id")
	private String emailId;
    
	@JsonProperty(value="gender")
	@Column(name = "Gender")
	private String gender;
    
	@JsonProperty(value="fatherName")
	@Column(name = "FatherName")
	private String fatherName;
    
	@JsonProperty(value="qualification")
	@Column(name = "Qualification")
	private String qualification;

	@JsonProperty(value="dateOfBirth")
	@Column(name = "DateOfBirth")
	private Date dateOfBirth;
    
	@JsonProperty(value="address1")
	@Column(name = "address1")
	private String address1;
    
	@JsonProperty(value="address2")
	@Column(name = "address2")
	private String address2;
    
	@JsonProperty(value="address3")
	@Column(name = "address3")
	private String address3;
    
	@JsonProperty(value="pinId")
	@Column(name = "Pin_id")
	private BigInteger pinId;
    
	@JsonProperty(value="anniversaryDate")
	@Column(name = "AnniversaryDate")
	private Date anniversaryDate;
    
	@JsonProperty(value="isMarried")
	@Column(name = "IsMarried")
	private String isMarried;
    
	@JsonProperty(value="gstIN")
	@Column(name = "GSTIN")
	private String gstIN;
    
	@JsonProperty(value="panNo")
	@Column(name = "PAN_NO")
	private String panNo;
    
	@JsonProperty(value="aadharCardNo")
	@Column(name = "AADHAR_CARD_NO")
	private String aadharCardNo;
    
	@JsonProperty(value="dLNo")
	@Column(name = "DL_NO")
	private String dLNo;
    
	@JsonProperty(value="annualIncome")
	@Column(name = "ANNUAL_INCOME")
	private String annualIncome;
    
	@JsonProperty(value="occupationID")
	@Column(name = "Occupation_ID")
	private BigInteger occupationID;
    
	@JsonProperty(value="landInAcres")
	@Column(name = "LAND_IN_ACRES")
	private Float landInAcres;
    
	@JsonProperty(value="latitude")
	@Column(name = "Latitude")
	private String latitude;

	@JsonProperty(value="longitude")
	@Column(name = "Longitude")
	private String longitude;
    
	@JsonProperty(value="createdBy")
	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
    
	@JsonProperty(value="createdDate")
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate =new Date();
    
	@JsonProperty(value="modifiedBy")
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
    
	@JsonProperty(value="modifiedDate")
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@JsonProperty(value="status1")
	@Column(name = "Status")
	private String status1;

	
}
