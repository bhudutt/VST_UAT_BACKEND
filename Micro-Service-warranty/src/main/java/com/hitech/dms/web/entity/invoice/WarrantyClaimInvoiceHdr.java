package com.hitech.dms.web.entity.invoice;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mahesh.kumar
 */
@Entity
@Getter
@Setter
@Table(name = "WA_CLAIM_INVOICE_HDR")
public class WarrantyClaimInvoiceHdr {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name="branch_id", updatable=false)
	private BigInteger branchId;
	
	@Column(name = "wcr_id")
    private BigInteger wcrId;

    @Column(name = "invoice_no")
    private String invoiceNo ;

    @Column(name = "invoice_date")
    private Date invoiceDate;
    
    @Column(name = "draft_flag", columnDefinition = "boolean default false")
    private Boolean draftFlag;

    @Column(name = "status")
    private String status;
    
    @Column(name = "cancelled_flag", columnDefinition = "boolean default false")
    private Boolean cancelledFlag;
    
    @Column(name = "customer_id")
    private BigInteger customerId;

    @Column(name="created_by", updatable=false)
    private BigInteger createdBy;
    
    @Column(name="created_on", updatable=false)
    private Date createdOn;

    @Column(name = "last_modified_by")
    private BigInteger lastModifiedBy;
    
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    
    @Column(name = "base_price")
    private BigDecimal basePrice;
    
    @Column(name = "gst_amount")
    private BigDecimal gstAmount;
    
    @Column(name = "invoice_amount")
    private BigDecimal invoiceAmount;
    
    @Column(name = "credit_note_no")
    private String creditNoteNo;
    
    @Column(name = "credit_note_date")
    private Date creditNoteDate;
    
    @Column(name = "credit_note_amount")
    private BigDecimal creditNoteAmount;
    
    
    @Column(name = "sv_handling_material")
    private String material;
    
    @Column(name = "sv_handling_unit_price")
    private BigDecimal unitPrice;
    
    @Column(name = "sv_handling_quantity")
    private BigDecimal quantity;
    
    @Column(name = "sv_handling_amount")
    private BigDecimal amount;
    
    @Column(name = "sv_handling_sac")
    private String sac;
    
    @Column(name = "sv_handling_tax")
    private BigDecimal taxPercent;
    
    @Column(name = "sv_handling_tax_value")
    private BigDecimal taxValue;
    
    @Column(name = "sv_handling_total_value")
    private String totalValue;
    
    @Column(name = "customer_invoice_no")
    private String customerInvoiceNo;
    
    @Column(name = "customer_invoice_date")
    private String customerInvoiceDate;
    
    
    
    
    
    @OneToMany(mappedBy = "warrantyClaimInvoiceHdr",cascade =  CascadeType.ALL)
    @JsonManagedReference
    private List<WarrantyClaimInvoiceDtl> warrantyClaimInvoiceDtl;
    
    //added mapping for labour and outside labour details on 23-10-2024
    
    @OneToMany(mappedBy = "warrantyClaimInvoiceHdr",cascade =  CascadeType.ALL)
    @JsonManagedReference
    private List<WarrantyClaimInvoiceLbrDtl> warrantyClaimInvoiceLbrDtl;
    
    @OneToMany(mappedBy = "warrantyClaimInvoiceHdr",cascade =  CascadeType.ALL)
    @JsonManagedReference
    private List<WarrantyClaimInvoiceOutLbrDtl> warrantyClaimInvoiceOutLbrDtl;
    
    @Transient
    private String finalSubmitFlag;

}
