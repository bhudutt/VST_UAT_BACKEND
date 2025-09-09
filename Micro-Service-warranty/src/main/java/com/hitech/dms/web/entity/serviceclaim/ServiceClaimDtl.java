package com.hitech.dms.web.entity.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_SERVICE_CLAIM_DTL")
public class ServiceClaimDtl {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@ManyToOne
    @JsonBackReference
    @JoinColumn(name = "CLAIM_HDR_ID", referencedColumnName = "id")
    private ServiceClaimHdr serviceClaimHdr;

	@Column(name = "RO_ID")
	private BigInteger roId;
	
	@Column(name = "PDI_ID")
	private BigInteger pdiId;
	
	@Column(name = "INSTALLATION_ID")
	private BigInteger installationId;
	
	@Column(name = "DC_ID")
	private BigInteger dcId;
	
	@Column(name = "JOBCARD_CAT_ID")
	private BigInteger JobcardCatId;
	
	@Column(name = "SERVICE_TYPE_ID")
	private BigInteger serviceTypeId;
	
	@Column(name = "CLAIM_VALUE")
	private BigDecimal claimValue;
	
	@Column(name = "PLANT_CODE")
	private String plantCode;
	
	@Column(name = "Status")
	private String status;
	
}
