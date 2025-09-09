package com.hitech.dms.web.entity.partycode;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "ADM_BP_PARTY_BRANCH")
@Data
public class PartyCodeEntity {

	@Id
	@Column(name = "party_branch_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger partyBranchId;
	
	@Column(name = "Branch_ID")
	private BigInteger branchId;
	
	@Column(name = "party_category_id")
	private BigInteger partyCategoryId;
	
	@Column(name = "PartyCode")
	private String partyCode;
	
	@Column(name="dealerCode")
	private String dealerCode;
	@Column(name = "To_branch_id")
	private BigInteger tobranchId;
	
	@Column(name = "financierID")
	private BigInteger financierID;
	
	@Column(name = "PartyTitle")
	private String partyTitle;
	
	@Column(name = "PartyName")
	private String partyName;
	
	@Column(name = "Party_Location")
	private String partyLocation;
	
	@Column(name="partyStatus")
	private String partyStatus;
	
	@Column(name="po_category_id")
	private int outletCategoryId;
	
	@Column(name = "ContactTitle")
	private String contactTitle;
	
	@Column(name = "dsr")
	private String dsr;
	
	
	
	@Column(name = "FirstName")
	private String firstName;
	
	@Column(name = "MiddleName")
	private String middleName;
	
	@Column(name = "LastName")
	private String lastName;
	
	@Column(name = "Email1")
	private String email1;
	
	@Column(name = "MobileNumber")
	private String mobileNumber;
	
	@Column(name = "Designation")
	private String designation;
	
	@Column(name = "PartyAddLine1")
	private String partyAddLine1;
	
	@Column(name = "PartyAddLine2")
	private String partyAddLine2;
	
	@Column(name = "PartyAddLine3")
	private String partyAddLine3;
	
	@Column(name = "pin_id")
	private BigInteger pinId;
	
	@Column(name = "Telephone")
	private String telephone;
	
	@Column(name = "Fax")
	private String fax;
	
	@Column(name = "Email2")
	private String email2;
	
	@Column(name = "CST")
	private String cst;
	
	@Column(name = "LST")
	private String lst;
	
	@Column(name = "PAN_OR_TAN")
	private String panOrTan;
	
	@Column(name = "GST_NUMBER")
	private String gstNumber;
	
	@Column(name = "AADHAR_NUMBER")
	private String aadharNumber;
	
	@Column(name = "PartsDiscountPercentage")
	private Integer partsDiscountPercentage;
	
	@Column(name = "LabourDiscountPercentage")
	private Integer labourDiscountPercentage;
	
	@Column(name = "CreditDays")
	private Integer creditDays;
	
	@Column(name = "CreditAmt")
	private Double creditAmt;
	
	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "IsOEMParty")
	@Type(type = "yes_no")
	private Boolean isOEMParty;
	
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	
	@Column(name="document1")
	private String document1;
	
	@Column(name="document2")
	private String document2;
	
	@Column(name="document3")
	private String document3;
	
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
}
