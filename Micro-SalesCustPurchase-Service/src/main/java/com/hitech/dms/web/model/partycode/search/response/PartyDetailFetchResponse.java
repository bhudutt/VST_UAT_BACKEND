package com.hitech.dms.web.model.partycode.search.response;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Entity
@Table(name = "ADM_BP_PARTY_BRANCH")
@Data
//@JsonPropertyOrder({"partyBranchId","action","id","partyCode","partyName","partyLocation","partyStatus","contactTitle","firstName","middleName","lastName","email1","mobileNumber","designation","status"})
@JsonIgnoreProperties({"isOEMParty","creditAmt","creditDays","labourDiscountPercentage","partsDiscountPercentage","lst","cst","fax","telephone","financierID","createdBy","createdDate"})
public class PartyDetailFetchResponse {
		
@Column(name = "party_branch_id")	
private BigInteger partyBranchId;
	
	private String action;
	
	@Column(name = "party_branch_id")	
	private BigInteger branchId;
	
	@Column(name = "party_category_id")	
	private BigInteger partyCategoryId;
	
	@Column(name = "PartyCode")	
	private String partyCode;
	
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
	
	@Column(name = "ContactTitle")	
	private String contactTitle;

	@Column(name = "FirstName")	
	private String firstName;
	
	@Column(name = "MiddleName")	
	private String middleName;
	
	@Column(name = "LastName")	
	private String lastName;
	
	@Column(name = "party_branch_id")	
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
	private Boolean isOEMParty;
	
	@Column(name = "isActive")	
	private char isActive;

	@Column(name = "CreatedBy")	
	private String createdBy;

	@Column(name = "CreatedDate")	
	private Date createdDate;

	@Column(name = "ModifiedBy")	
	private String modifiedBy;

	@Column(name = "ModifiedDate")	
	private Date modifiedDate;
	
	@Column(name = "PartyStatus")	
	private String status;
	
	@Column(name="dsr")
	private String dsr;
	
	
	
	@Column(name = "PartyStatus")	
	private String partyStatus;
	
	
	
	@Column(name="document1")
	private String document1;
	
	
	@Column(name="document2")
	private String document2;
	
	@Column(name="document3")
	private String document3;
	
	@Column(name="ParentDealerName")
	private String parentDealerName;
	
	@Column(name = "BranchName")
	private String branchName;
	
	@Column(name="dealerCode")
	private String dealerCode;

	
	

	



}
