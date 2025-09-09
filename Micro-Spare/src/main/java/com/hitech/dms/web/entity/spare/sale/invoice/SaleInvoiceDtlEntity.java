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
@Table(name = "PA_SALE_INVOICE_DTL")

public class SaleInvoiceDtlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="invoice_sale_dtl_id")
	private BigInteger invoiceSaleDtlId;
	
	@Column(name="invoice_sale_id")
	private BigInteger invoiceSaleId;
	
	@Column(name="customer_hdr_id")
	private BigInteger customerHdrId;
	
	@Column(name="dc_id")
	private BigInteger dcId;
	
	@Column(name="partbranch_id")
	private BigInteger partBranchId;
	
	@Column(name="part_id")
	private BigInteger partId;
	
	@Column(name="stock_bin_id")
	private BigInteger stockBinId;
	
	@Column(name="po_dtl_id")
	private BigInteger poDtlId;
	
	@Column(name="customer_dtl_id")
	private BigInteger customerDtlId;
	
	@Column(name="dc_dtl_id")
	private BigInteger dcDtlId;
	
	
	@Column(name="Pick_List_Dtl_Id")
	private BigInteger pickListDtlId;

	@Column(name="HsnCode")
	private String hsnCode;
	
	@Column(name="NDP")
	private BigDecimal ndp;

	@Column(name="MRP")
	private BigDecimal mrp;
	
	@Column(name="SellingPrice")
	private BigDecimal sellingPrice;	

	@Column(name="isIndividualBin")
	private Character IsIndividualBin;
	
	@Column(name="Qty")
	private BigDecimal qty;

	@Column(name="OrderQty")
	private BigDecimal orderQty;
	
	@Column(name="BasicValue")
	private BigDecimal basicValue;
	
	@Column(name="DiscountType")
	private String discountType;
	
	@Column(name="DiscountRate")
	private BigDecimal discountRate;
	
	@Column(name="DiscountValue")
	private BigDecimal discountValue;
	
	@Column(name="RetunedQty")
	private BigDecimal retunedQty;
	
	@Column(name="TaxValue")
	private BigDecimal taxValue;
	
	@Column(name="cgstPercent")
	private Integer cgstPercent;
	
	@Column(name="sgstPercent")
	private Integer sgstPercent;
	
	@Column(name="igstPercent")
	private Integer igstPercent;
	
	@Column(name="BillValue")
	private BigDecimal billValue;
	
	@Column(name="IsCancelled")
	private char isCancelled;
	
	@Column(name="CancelledBy")
	private String cancelledBy;
	
	@Column(name="CancelledOn")
	private Date cancelledOn;
	
	@Column(name="CancelReason")
	private String cancelReason;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@Column(name="RefDocDetailId")
	private BigInteger refDocDetailId;
	
	@Column(name="Sale_On")
	private Date saleOn;
	
	@Column(name="Add_Discount_type")
	private String splDiscountType;	
	
	@Column(name="Add_Discount_Rate")
	private BigDecimal splDiscountRate;	
	
	@Column(name="Add_Discount_Amount")
	private BigDecimal splDiscountAmount;	
	
	@Column(name="ChasisNo")
	private String chasisNo;	

	@Column(name="Version_No")
	private Integer versionNo;	
	
	@Column(name="apr_returned_qty")
	private Integer aprReturnedQty;
}
