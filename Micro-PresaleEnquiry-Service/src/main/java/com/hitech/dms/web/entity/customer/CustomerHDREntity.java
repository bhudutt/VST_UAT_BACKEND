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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "CM_CUST_HDR")
@Data
public class CustomerHDREntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 967041673311533037L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Customer_Id")
	private BigInteger customerId;

	@Column(name = "ProspectType")
	private String prospectType;

	@Column(name = "CustomerGroup_Id")
	private BigInteger customerGroupId;

	@Column(name = "CustomerCategory_Id")
	private BigInteger customerCategoryId;

	@Column(name = "CustomerCode", updatable = false)
	private String customerCode;

	@Column(name = "ContactTitle")
	private String contactTitle;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "MiddleName")
	private String middleName;

	@Column(name = "LastName")
	private String LastName;

	@Column(name = "Mobile_No")
	private String mobileNo;

	@Column(name = "Alternate_No")
	private String alternateNo;

	@Column(name = "WhatsappNo")
	private String whatsAppNo;
	
	@Column(name = "PhoneNumber")
	private String phoneNo;

	@Column(name = "Organization_Name")
	private String organizationName;

	@Column(name = "Email_id")
	private String emailId;

	@Column(name = "Gender")
	private String gender;

	@Column(name = "FatherName")
	private String fatherName;

	@Column(name = "Qualification")
	private String qualification;

	@Column(name = "DateOfBirth")
	private Date dateOfBirth;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address3")
	private String address3;

	@Column(name = "Pin_id")
	private BigInteger pinId;

	@Column(name = "AnniversaryDate")
	private Date anniversaryDate;

	@Column(name = "IsMarried")
	private String isMarried;

	@Column(name = "GSTIN")
	private String gstIN;

	@Column(name = "PAN_NO")
	private String panNo;

	@Column(name = "AADHAR_CARD_NO")
	private String aadharCardNo;

	@Column(name = "DL_NO")
	private String dLNo;

	@Column(name = "ANNUAL_INCOME")
	private String annualIncome;

	@Column(name = "Occupation_ID")
	private BigInteger occupationID;

	@Column(name = "LAND_IN_ACRES")
	private Float landInAcres;

	@Column(name = "Latitude")
	private String latitude;

	@Column(name = "Longitude")
	private String longitude;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(mappedBy = "customerHdr")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<CustomerAddDTLEntity> customerAddDTLList;
	
	@OneToMany(mappedBy = "customerHdr")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<CustomerFleetDTLEntity> customerFleetDTLList;
}
