package com.hitech.dms.web.model.partycode.search.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"partyBranchId","action","id","partyCode","partyName","partyLocation","partyStatus","dealerCode","contactTitle","branchId","firstName","middleName","lastName","email1","mobileNumber","designation","status","partyCategoryId"})
@JsonIgnoreProperties({"isActive","partyTitle","isOEMParty","creditAmt","creditDays","labourDiscountPercentage","partsDiscountPercentage","aadharNumber","gstNumber","panOrTan","lst","cst","email2","fax","telephone","pinId","tobranchId","financierID","createdBy","createdDate","modifiedBy","modifiedDate"})
public class PartyCodeSearchResponseModel {
	
	
	private BigInteger partyBranchId;
	
	private String action;
	
	private BigInteger branchId;
	
	private BigInteger partyCategoryId;
	
	@JsonProperty("Party Code")
	private String partyCode;
	
	private String dealerCode;
	
	private BigInteger tobranchId;
	
	private BigInteger financierID;
	
	private String partyTitle;
	
	@JsonProperty("Party Name")
	private String partyName;
	
	@JsonProperty("Party Location")
	private String partyLocation;
	
	@JsonProperty("Contact Title")
	private String contactTitle;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String email1;
	
	private String mobileNumber;
	
	private String designation;
	
	@JsonProperty("Address 1")
	private String partyAddLine1;
	
	@JsonProperty("Address 2")
	private String partyAddLine2;
	
	@JsonProperty("Address 3")
	private String partyAddLine3;
	
	private BigInteger pinId;
	
	private String telephone;
	
	private String fax;
	
	private String email2;
	
	private String cst;
	
	private String lst;
	
	private String panOrTan;
	
	private String gstNumber;
	
	private String aadharNumber;
	
	private Integer partsDiscountPercentage;
	
	private Integer labourDiscountPercentage;
	
	private Integer creditDays;
	
	private Double creditAmt;
	
	private String remarks;
	
	private Boolean isOEMParty;
	
	private Boolean isActive;
	
	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String status;
	
	private String partyStatus;
	

	


}
