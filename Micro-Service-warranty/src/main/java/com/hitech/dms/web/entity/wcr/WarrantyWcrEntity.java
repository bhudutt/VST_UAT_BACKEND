package com.hitech.dms.web.entity.wcr;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "SV_WA_WCR")
public class WarrantyWcrEntity implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@Column(name = "branch_id", updatable=false)
	private BigInteger branchId;

    @Column(name = "wcr_no", unique = true,length = 21)
    private String wcrNo;

    @Column(name = "wcr_date")
    private LocalDateTime wcrDate;

    @Column(name = "wcr_status")
    private String wcrStatus = "Waiting for Approval";

    @Column(name = "wcr_type")
    private String wcrType;

    @Column(name = "draft_flag", columnDefinition = "boolean default false")
    private Boolean draftFlag;

//    @ManyToOne
//    @JoinColumn(name = "warranty_pcr_id")
//    private ServiceWarrantyPcr warrantyPcr;
    
    @Column(name = "warranty_pcr_id")
    private Long warrantyPcrId;
    

//    @ManyToOne
//    @JoinColumn(name = "warranty_gwl_id")
//    private WarrantyGoodwill warrantyGoodwill;
    @Column(name = "warranty_gwl_id")
    private Long warrantyGoodwillId;
    

    @Column(name = "vst_remarks")
    private String vstRemarks;

    @Column(name = "inspection_remarks")
    private String inspectionRemarks;

    @Column(name = "created_by", updatable=false)
    private BigInteger createdBy;
    
    @Column(name = "created_on", updatable=false)
    private Date createdOn;
    
    @Column(name = "modified_by")
    private BigInteger modifiedBy;
    
    @Column(name = "modified_on")
    private Date modifiedOn;

//    @ManyToOne
//    @JoinColumn(name = "inspection_by")
//    private KubotaEmployeeMaster inspectionBy;
    @Column(name = "inspection_by")
    private Long inspectionBy;

    @Column(name = "inspection_date")
    private Date inspectionDate;
    
    //added by mahesh.kumar on 30-01-2024
    @Column(name = "final_status")
    private String finalStatus;
    
//    @Column(name = "manual_invoice_no")
//    private String manualinvoiceNo;
   
//    @Column(name = "manual_invoice_date")
//    private Date manualinvoiceDate;

//    @ManyToMany(mappedBy = "warrantyWcr")
//    private List<WarrantyDeliveryChallan> warrantyDeliveryChallans = new ArrayList<>();
}
