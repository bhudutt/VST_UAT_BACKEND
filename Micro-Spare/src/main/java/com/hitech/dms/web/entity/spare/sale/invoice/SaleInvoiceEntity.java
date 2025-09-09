package com.hitech.dms.web.entity.spare.sale.invoice;
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
@Table(name = "PA_SALE_INVOICE")

public class SaleInvoiceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="invoice_sale_id")
	private BigInteger invoiceSaleId;
	
	@Column(name="branch_id")
	private BigInteger branchId;
	
	@Column(name="DocType")
	private BigInteger DocType;
	
	@Column(name="DocNumber")
	private String DocNumber;
	
	@Column(name="DocDate")
	private Date DocDate;
	
	@Column(name="po_hdr_id")
	private BigInteger poHdrId;
	
	@Column(name="CategoryCode")
	private String categoryCode;

	@Column(name="PartyCode")
	private String partyCode;
	
	@Column(name="Customer_Id")
	private BigInteger customerId;
	
	@Column(name="CustomerName")
	private String customerName;	

	@Column(name="PartyAddLine1")
	private String partyAddLine1;
	
	@Column(name="city_id")
	private BigInteger cityId;
	
	@Column(name="pin_id")
	private BigInteger pinId;
	
	@Column(name="taxctgry_branch_id")
	private BigInteger taxctgryBranchId;
	
	@Column(name="DiscountType")
	private String discountType;
	
	@Column(name="DiscountRate")
	private BigDecimal discountRate;
	
	@Column(name="TDiscountValue")
	private BigDecimal tDiscountValue;
	
	@Column(name="TBasicValue")
	private BigDecimal tBasicValue;
	
	@Column(name="TNetValue")
	private BigDecimal tNetValue;
	
	@Column(name="TTaxValue")
	private BigDecimal tTaxValue;
	
	@Column(name="TTaxableValue")
	private BigDecimal tTaxableValue;
	
	@Column(name="TBillValue")
	private BigDecimal tBillValue;
	
	@Column(name="TransportationCharges")
	private BigDecimal transportationCharges;
	
	@Column(name="otherCharges")
	private BigDecimal otherCharges;

	@Column(name="Remarks")
	private String remarks;
	
	@Column(name="BillTitle")
	private String billTitle;
	
	@Column(name="TransporterName")
	private String transporterName;

	@Column(name="TransporterVehicleNo")
	private String transporterVehicleNo;
	
	@Column(name="LR_No")
	private String lrNo;	
	
	@Column(name="LR_DATE")
	private Date lrDate;	
	
	@Column(name="Spl_Discount_type")
	private String splDiscountType;	
	
	@Column(name="Spl_Discount_Rate")
	private BigDecimal splDiscountRate;	
	
	@Column(name="Total_Spl_DiscountValue")
	private BigDecimal totalSplDiscountValue;
	
	@Column(name="TCS_Percent")
	private BigDecimal tcsPercent;
	
	@Column(name="TCS_Amount")
	private BigDecimal tcsAmount;
	
	@Column(name="lastUpdatedDate")
	private Date lastUpdatedDate;
	
	@Column(name="View_Doc_No")
	private String viewDocNo;	
	
	@Column(name="Version_No")
	private Integer versionNo;	
	
	@Column(name="Registration_No")
	private String registrationNo;	
	
	@Column(name="Mobile_No")
	private String mobileNo;	

	@Column(name="GST_NO")
	private String gstNo;	

	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@Column(name="FP_Amount")
	private BigDecimal forwardingPackagingAmount;
	
	@Column(name="FP_GST")
	private BigDecimal forwardingPackagingGst;
	
	@Column(name="Part_Gst")
	private BigDecimal forwardGstAmount;
}
