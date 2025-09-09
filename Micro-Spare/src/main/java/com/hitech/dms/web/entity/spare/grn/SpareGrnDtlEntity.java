package com.hitech.dms.web.entity.spare.grn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_GRN_DTL")
public class SpareGrnDtlEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mrn_dtl_id")
	private BigInteger grnDtlId;
	
	@Column(name="mrn_hdr_id")
	private BigInteger grnHdrId;
	
	@Column(name="sr_no")
	private Integer srNo;
	
	@Column(name="FinalPOnumber")
	private String finalPoNumber;
	
	@Column(name="POnumber")
	private String poNumber;
	
	@Column(name="ItemNo")
	private String itemNo;
	
	@Column(name="ref_partBranch_id")
	private Integer refPartBranchId;

	@Column(name="branch_store_id")
	private Integer branchStoreId;	
	
	@Column(name="stock_bin_id")
	private BigInteger stockBinId;

	@Column(name="C_InvoiceRate")
	private BigDecimal cInvoiceRate;

	@Column(name="I_InvoiceQty")
	private BigDecimal iInvoiceQty;

	@Column(name="i_NDP")
	private BigDecimal iNdp;
	
	@Column(name="I_MRP")
	private BigDecimal iMrp;
	
	@Column(name="I_NetValue")
	private BigDecimal iNetValue;
	
	@Column(name="I_DiscountValue")
	private BigDecimal iDiscountValue;
	
	@Column(name="I_TaxValue")
	private BigDecimal iTaxValue;
	
	@Column(name="I_GrossValue")
	private BigDecimal iGrossValue;
	
	@Column(name="ReceivedNormalQty")
	private BigDecimal receivedNormalQty;
	
	@Column(name="ReceivedNormalAmt")
	private BigDecimal receivedNormalAmt;
	
	@Column(name="ReceivedDamagedQty")
	private BigDecimal receivedDamagedQty;
	
	@Column(name="ReceivedDamagedAmt")
	private BigDecimal receivedDamagedAmt;
	
	@Column(name="ReceivedShortQty")
	private BigDecimal receivedShortQty;
	
	@Column(name="ReceivedShortAmt")
	private BigDecimal receivedShortAmt;
	
	@Column(name="WrongPartNum")
	private String wrongPartNum;
	
	@Column(name="NetReceivedQty")
	private BigDecimal netReceivedQty;
	
	@Column(name="NetReceivedAmt")
	private BigDecimal netReceivedAmt;
	
	@Column(name="HandlingCharges")
	private BigDecimal handlingCharges;
	
	@Column(name="FreightCharges")
	private BigDecimal freightCharges;
	
	@Column(name="OtherCharges")
	private BigDecimal otherCharges;
	
	@Column(name="TotalCharges")
	private BigDecimal totalCharges;
	
	@Column(name="C_NetAmt")
	private BigDecimal cNetAmt;
	
	@Column(name="C_LandedCost")
	private BigDecimal cLandedCost;
	
//	@Column(name="ClaimType")
//	private String ClaimType;

//	@Column(name="IsClaimed")
//	private boolean isClaimed;

//	@Column(name="ClaimedQty")
//	private BigDecimal claimedQty;
	
	@Column(name="ReceivedStatus")
	private boolean receivedStatus;
	
	@Column(name="IsFOC")
	private boolean isFoc;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="CGST")
	private BigDecimal cgst;
	
	@Column(name="SGST")
	private BigDecimal sgst;
	
	@Column(name="IGST")
	private BigDecimal igst;
	
	@Column(name="Version_No")
	private String versionNo;
	
	@Column(name="CostPerUnit")
	private BigDecimal costPerUnit;
	
	@Column(name="RestrictedQty")
	private BigDecimal restrictedQty;
	
	@Column(name="UnrestrictedQty")
	private BigDecimal unrestrictedQty;
	
	@Column(name="Bin_Name")
	private String binName;
	
}
