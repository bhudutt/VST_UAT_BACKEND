package com.hitech.dms.web.entity.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_SERVICE_CLAIM_HDR")
public class ServiceClaimHdr {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name = "CLAIM_TYPE_ID")
	private BigInteger claimTypeId;
	
	@Column(name = "CLAIM_NO")
	private String claimNo;
	
	@Column(name = "CLAIM_DATE")
	private Date claimDate;
	
	@Column(name = "CLAIM_AMOUNT")
	private BigDecimal claimAmount;
	
	@Column(name = "STATUS")
	private String status;
	
	@OneToMany(mappedBy = "serviceClaimHdr",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceClaimDtl> serviceClaimDtls;
	
	@Column(name = "CREATEDBY")
	private BigInteger createdBy;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate;
	
	@Column(name = "MODIFIEDBY")
	private BigInteger modifiedBy;
	
	@Column(name = "MODIFIEDDATE")
	private Date modifiedDate;
	
}
