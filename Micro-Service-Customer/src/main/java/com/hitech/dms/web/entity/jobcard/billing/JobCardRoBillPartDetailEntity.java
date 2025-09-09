package com.hitech.dms.web.entity.jobcard.billing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="SV_RO_BILL_PRT_DTL")
public class JobCardRoBillPartDetailEntity implements Serializable {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Column(name="ro_bill_prt_dtl_id")
	    private BigInteger roBillPartDetailId;
//	    @ManyToOne
//	    @JoinColumn(name = "ro_bill_id")
//	    private JobCardBillingHDREntity jobCardBillingHDREntity;
	   @Column(name="ro_bill_id")
	    private BigInteger roBillId;
	    @Column(name="requisition_id")
	    private BigInteger requisitionId;
	    @Column(name="partBranch_id")
	    private BigInteger partBranchId;
	    @Column(name="Qty")
	    private BigDecimal qty;
	    @Column(name="Rate")
	    private BigDecimal rate;
	    @Column(name="DiscountType")
	    private String discountType;
	    @Column(name="DiscountRate")
	    private BigDecimal discountRate;
	    @Column(name="DiscountValue")
	    private BigDecimal discountValue;
	    @Column(name="BasicValue")
	    private BigDecimal basicValue;
	    @Column(name="TaxValue")
	    private BigDecimal taxValue;
	    @Column(name="BillValue")
	    private BigDecimal billValue;
	    @Column(name="PartWarrantyRate")
	    private BigDecimal partWarrantyRate;
	    @Column(name="NDP")
	    private BigDecimal ndp;
	    @Column(name="MRP")
	    private BigDecimal mrp;
	    @Column(name="GNDP")
	    private BigDecimal gndp;
	    @Column(name="billable_type_id")
	    private BigInteger billableTypeId;
	    @Column(name="OEMPaidPercentage")
	    private BigDecimal oemPaidPercentage;
	    @Column(name="CustomerPaidPercentage")
	    private BigDecimal customerPaidPercentage;
	    @Column(name="DealerPaidPercentage")
	    private BigDecimal dealerPaidPercentage;
	    @Column(name="InsurancePaidPercentage")
	    private BigDecimal insurancePaidPercentage;
	    @Column(name="IsClaimed")
	    private Character isClaimed;
	    @Column(name="claim_id")
	    private BigInteger claimId;
	    @Column(name="CreatedDate")
	    private Date createdDate=new Date();
	    @Column(name="CreatedBy")
	    private String createdBy;
	    @Column(name="ModifiedDate")
	    private Date modifiedDate=new Date();
	    @Column(name="ModifiedBy")
	    private String modifiedBy;
	    @Column(name="CauORCon")
	    private String cauORCon;
	    @Column(name="tempBillableTypeId")
	    private Integer tempBillableTypeId;
	    @Column(name="tempCauOrConPartType")
	    private String tempCauOrConPartType;
	    @Column(name="GroupCode")
	    private Integer groupCode;
	    @Column(name="ComplaintCode")
	    private String complaintCode;
	    @Column(name="Version_No")
	    private Integer versionNo;
	    @Column(name="ro_id")
	    private BigInteger roId;
	    @Column(name="Cgst")
	    private Integer cgst;
	    @Column(name="CgstAMT")
	    private BigDecimal cgstAmt;
	    @Column(name="Sgst")
	    private Integer sgst;
	    @Column(name="SgstAMT")
	    private BigDecimal sgstAmt;
	    @Column(name="Igst")
	    private Integer igst;
	    @Column(name="IgstAMT")
	    private BigDecimal igstAmt;
	    @Column(name="HSNCODE")
	    private Integer hsnCode;
	    @Column(name="TotalAMT")
	    private BigDecimal totalAmt; 

}
