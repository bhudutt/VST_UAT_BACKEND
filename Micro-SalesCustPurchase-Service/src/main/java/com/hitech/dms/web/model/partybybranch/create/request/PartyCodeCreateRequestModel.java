package com.hitech.dms.web.model.partybybranch.create.request;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PartyCodeCreateRequestModel {

	private BigInteger partyBranchId;
	
	private BigInteger branchId;
	
	private String dealerCode;
	private BigInteger partyCategoryId;
	
	private String partyCode;
	
	private BigInteger tobranchId;
	
	private BigInteger financierID;
	
	private String partyTitle;
	
	private String partyName;
	
	private String partyLocation;
	
	private String contactTitle;
	private String partyStatus;
	private String dsr;
	private Integer outletCategory[];
	
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
	
	private String status;
	
	
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
	private MultipartFile document1;
	private MultipartFile document2;
	private MultipartFile document3;
	
	private Integer page;
	private Integer size;
	

}
