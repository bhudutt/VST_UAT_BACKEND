package com.hitech.dms.web.model.spare.sale.aprReturn.domain;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name = "APR_RETURN_HRD")
@Data
public class AprReturnHrdEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="APR_RETURN_ID")
	private BigInteger aprReturedId;
	
	@Column(name="BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name="APR_RETURN_DOC_NO")
	private String aprReturedDocNo;
	
	@Column(name="APR_RETURN_DATE")
	private Date aprReturnDate;
	
	@Column(name="PARTY_ID")
	private BigInteger partyId;
	
	@Column(name="INVOICE_ID")
	private BigInteger invoiceId;
	
	@Column(name="APR_RETURN_STATUS")
	private String aprReturnStatus;
	
	@Column(name="DELIVERY_CHALLAN_ID")
	private BigInteger deliveryChallanId;
	
	// Mapping to the other table
    @OneToMany(cascade = CascadeType.ALL)
    private Set<AprReturnDtlEntity> aprReturnDtlEntity;
	
    @Column(name="CREATED_DATE")
	private Date createdDate;
	
    @Column(name="CREATED_BY")
	private BigInteger createdBy;
    
    @Column(name="MODIFIED_DATE")
  	private Date modifiedDate;
  	
    @Column(name="MODIFIED_BY")
  	private BigInteger modifiedBy;
    
}
