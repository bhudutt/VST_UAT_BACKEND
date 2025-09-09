package com.hitech.dms.web.model.partybybranch.create.request;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PartyCodeRequestModel {
	
	
private BigInteger partyBranchId;
	
	private BigInteger branchId;
	
	private BigInteger partyCategoryId;
	
	private String partyCode;
	private String dealerCode;
	
	private BigInteger tobranchId;
	
	private BigInteger financierID;
	
	private String partyTitle;
	
	private String partyName;
	
	private String partyLocation;
	
	private String contactTitle;
	private String partyStatus;
	private String dsr;
	private String outletCategoryId;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String email1;
	
	private String mobileNumber;
	
	private String designation;
	
	private String partyAddLine1;
	
	private String partyAddLine2;
	
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
	private String panNo;
	
	
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
	private String document1;
	private String document2;
	private String document3;
	
	private Integer page;
	private Integer size;
	

}
