package com.hitech.dms.web.entity.deliverychallan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WA_DELIVERY_CHALLAN_HDR")
public class DeliveryChallanHdr {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
	
	@Column(name = "branch_id", updatable = false)
    private BigInteger branchId;
	
	@Column(name = "dc_no")
    private String dcNo;

    @Column(name = "dc_date", updatable = false)
    private Date dcDate = new Date();

    @Column(name = "draft_flag")
    private Boolean draftFlag;

    @Column(name = "status")
    private String status = "";
    
    @Column(name = "transporter_name")
    private String transporterName;
    
    @Column(name = "lr_no")
    private String lrNo;
    
    @Column(name = "lr_date")
    private Date lrDate;
    
    @Column(name = "base_amount")
    private BigDecimal baseAmount;
    
    @Column(name = "gst_amount")
    private BigDecimal gstAmount;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @OneToMany(mappedBy = "deliveryChallanHdr", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    		orphanRemoval = true)
    @JsonManagedReference
    private List<DeliveryChallanDtl> deliveryChallanDtls;
    
    @Column(name = "created_by")
    private BigInteger createdBy;
    
    @Column(name = "created_date")
    private Date createdDate;
    
    @Column(name = "last_modified_by")
    private BigInteger lastModifiedBy;
    
    @Column(name = "last_modified_date")
    private Date lastModifiedDate; 
	
}
