package com.hitech.dms.web.entity.spare.grn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_GRN_CLAIM_HDR")
public class SpareGrnClaimHdrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mrn_claim_hdr_id")
	private BigInteger grnClaimHdrId;
	
	@Column(name="mrn_hdr_id")
	private BigInteger grnHdrId;
	
	@Column(name="ClaimGenerationNumber")
	private String claimGenerationNumber;

	@Column(name="ClaimDate")
	private Date claimDate;
	
	@Column(name="ClaimType")
	private String claimType;
	
	@Column(name="ClaimStatus")
	private String claimStatus;
	
	@Column(name="IsAgree")
	private Character isAgree;
	
	@Column(name="ClaimRemarks")
	private String claimRemarks;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;
}
