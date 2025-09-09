package com.hitech.dms.web.entity.goodwill;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;

import lombok.Data;

@Data
@Entity
@Table(name="SV_WA_GOODWILL")
public class WarrantyGoodwill {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
    @Column(name = "branch_id", updatable = false)
    private BigInteger branchId;
    
    @ManyToOne
    @JoinColumn(name = "warranty_pcr_id")
    private ServiceWarrantyPcr serviceWarrantyPcr;

    @Column(name = "goodwill_no", unique = true,length = 21)
    private String goodwillNo;

    @Column(name = "goodwill_date", updatable = false)
    private Date goodwillDate = new Date();

    @Column(name = "draft_flag")
    private Boolean draftFlag;

    @Column(name = "status")
    private String status = "Waiting for Approval";
    
    @Column(name = "dealer_remark")
    private String dealerRemark;

    @Column(name = "ho_remark")
    private String hoRemark;

    @Column(name = "complaint_code_id")
    private Integer complaintCodeId;
    
    @Column(name = "complaint_aggregate_id")
    private Integer complaintAggregateId;
    
    @Column(name = "nature_of_failure")
    private String natureOfFailure;
    
    @Column(name = "hour")
    private BigInteger hour;
    
    @Column(name = "created_by", updatable = false)
    private BigInteger createdBy;
    
    @Column(name = "created_on",updatable = false)
    private Date createdOn = new Date();

    @Column(name = "last_modified_by")
    private BigInteger lastModifiedBy;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    
    @Column(name = "product_type")
    private String productType;

    @OneToMany(mappedBy = "warrantyGoodwill",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WarrantyGoodwillPhoto> warrantyGoodwillPhoto;

}
