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
@Table(name = "PA_GRN_HDR")
public class SpareGrnHdrEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mrn_hdr_id")
	private BigInteger grnHdrId;
	
	@Column(name="branch_id")
	private BigInteger branchId;
	
	@Column(name="mrnType_id")
	private BigInteger grnTypeId;
	
	@Column(name="MRNNumber")
	private String grnNumber;
	
	@Column(name="MRNDate")
	private Date grnDate;
	
	@Column(name="InventoryTransferNumber")
	private Date inventoryTransferNumber;

	@Column(name="party_id")
	private BigInteger partyId;

	@Column(name="status")
	private String status;

	@Column(name="branch_store_id")
	private BigInteger branchStoreId;

	@Column(name="Suplr_DCNo")
	private String suplrDcNo;
	
	@Column(name="Suplr_DCDate")
	private Date suplrDcDate;
	
	@Column(name="InvoiceNo")
	private String invoiceNo;
	
	@Column(name="InvoiceDate")
	private Date invoiceDate;
	
	@Column(name="DriverName")
	private String driverName;
	
	@Column(name="DriverMobNo")
	private String driverMobNo;
	
	@Column(name="Remarks")
	private String remarks;
	
	@Column(name="TransporterName")
	private String transporterName;
	
	@Column(name="TransporterVehicleNo")
	private String transporterVehicleNo;
	
	@Column(name="RoadPermitDetails")
	private String roadPermitDetails;
	
	@Column(name="TotalItems")
	private Integer totalItems;
	
	@Column(name="T_NetValue")
	private BigDecimal tNetValue;
	
	@Column(name="T_DiscountValue")
	private BigDecimal tDiscountValue;
	
	@Column(name="T_ExciseValue")
	private Integer tExciseValue;
	
	@Column(name="T_TaxValue")
	private BigDecimal tTaxValue;
	
	@Column(name="RoundOffValue")
	private Integer roundOffValue;
	
	@Column(name="T_GrossValue")
	private Integer tGrossValue;
	
	@Column(name="HandlingCharges")
	private Integer handlingCharges;
	
	@Column(name="FreightCharges")
	private Integer freightCharges;
	
	@Column(name="OtherCharges")
	private Integer otherCharges;
	
	@Column(name="MRNInvoiceAmount")
	private BigDecimal GrnInvoiceAmount;
	
	@Column(name="ShortAmount")
	private BigDecimal shortAmount;
	
	@Column(name="DamagedAmount")
	private BigDecimal damagedAmount;
	
	@Column(name="MRNValue")
	private BigDecimal grnValue;
	
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
	
	@Column(name="LR_No")
	private String lrNo;
	
	@Column(name="LR_Date")
	private Date lrDate;
	
	@Column(name="OtherExpenses")
	private BigDecimal otherExpenses;
	
	@Column(name="PONumber")
	private String poNumber;
	
	@Column(name="PODate")
	private Date poDate;
	
	@Column(name="POPlantId")
	private BigInteger poPlantId;
	
}
