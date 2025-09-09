package com.hitech.dms.web.entity.spare.grn;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_GRN_CLAIM_PHOTOS")
public class SpareGrnClaimPhotosEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private BigInteger id;
	
	@Column(name="mrn_claim_hdr_id")
	private BigInteger grnClaimHdrId;
	
	@Column(name="mrn_claim_dtl_id")
	private BigInteger grnClaimDtlId;

	@Column(name="file_type")
	private String fileType;
	
	@Column(name="file_name")
	private String fileName;

	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
}
