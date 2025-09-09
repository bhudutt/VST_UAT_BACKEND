package com.hitech.dms.web.entity.pcr;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name="SV_WA_PCR_HDR")
public class ServiceWarrantyPcr implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name="branch_id", updatable=false)
	private BigInteger branchId;
	
	@Column(name = "service_jobcard_id")
    private BigInteger serviceJobCardId;

    @Column(name = "pcr_no")
    private String pcrNo ;

    @Column(name = "pcr_date")
    private Date pcrDate;
    
    @Column(name = "draft_flag", columnDefinition = "boolean default false")
    private Boolean draftFlag;

    @Column(name = "status")
    private String status = "Waiting for Approval";

    @Column(name="created_by", updatable=false)
    private BigInteger createdBy;
    
    @Column(name="created_date", updatable=false)
    private Date createdDate;

    @Column(name = "last_modified_by")
    private BigInteger lastModifiedBy;
    
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(name = "complaint_code_id")
    private Integer complaintCodeId;
    
    @Column(name = "complaint_aggregate_id")
    private Integer complaintAggregateId;
    
    @Column(name = "nature_of_failure")
    private String natureOfFailure;
    
    @Column(name = "hour")
    private BigInteger hour;
    
    @Column(name = "pcr_submitted_Date")
    private Date pcrSubmittedDate;
    
    //added on 07-08-24
    @Column(name = "product_type_id")
    private Integer productTypeId;
    
    @OneToMany(mappedBy = "serviceWarrantyPcr",cascade = CascadeType.ALL,
    		fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private List<WarrantyPcrPhotos> warrantyPcrPhotos;
    
    @OneToMany(mappedBy = "serviceWarrantyPcr",cascade =  CascadeType.ALL, 
    		fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private List<ServiceWarrantyPcrDtlEntity> serviceWarrantyPcrDtlEntity;
    

}
