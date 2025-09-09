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
@Table(name="SV_RO_BILL_LBR_DTL")
public class JobCardRoBillLabourDTLEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ro_bill_lbr_dtl_id")
	private BigInteger roBillLbrDtlId;
	//@Column(name="ro_bill_id")
//	@ManyToOne
//    @JoinColumn(name = "ro_bill_id")
//    private JobCardBillingHDREntity jobCardBillingHDREntity;
	@Column(name = "ro_bill_id")
    private BigInteger roBillId;
	@Column(name="requisition_id")
    private BigInteger requisitionId;
	@Column(name="labour_id")
    private BigInteger labourId;
	@Column(name="StandardHrs")
    private BigDecimal standardHrs;
	@Column(name="Rate")
    private BigDecimal rate;
	@Column(name="FixedAmount")
    private BigDecimal fixedAmount;
	@Column(name="DiscountType")
    private String discountType;
	@Column(name="DiscountRate")
    private BigDecimal discountRate;
	@Column(name="DiscountValue")
    private BigDecimal discountValue;
	@Column(name="TaxValue")
    private BigDecimal taxValue;
	@Column(name="BasicValue")
    private BigDecimal basicValue;
	@Column(name="BillValue")
    private BigDecimal billValue;
	@Column(name="LabourWarrantyRate")
    private BigDecimal labourWarrantyRate;
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
    private Date createdDate =new Date();
	@Column(name="CreatedBy")
    private String createdBy;
	@Column(name="ModifiedDate")
    private Date modifiedDate= new Date();
	@Column(name="ModifiedBy")
    private String modifiedBy;
	@Column(name="Version_No")
    private Integer versionNo;
	@Column(name="GroupCode")
    private String groupCode;
	@Column(name="ComplaintCode")
    private String complaIntegerCode;
	@Column(name="RO_ID")
    private Integer roId;
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
