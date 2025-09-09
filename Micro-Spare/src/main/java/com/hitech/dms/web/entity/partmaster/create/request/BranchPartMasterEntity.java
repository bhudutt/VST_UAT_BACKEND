package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "PA_PART_BRANCH")
@Data
public class BranchPartMasterEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "partBranch_id")
	private Integer partBranchId;

	@Column(name = "part_id")
	private Integer partId;

	@Column(name = "DocType")
	private String docType;

	@Column(name = "DocName")
	private String docName;

	@Column(name = "DocPath")
	private String docPath;

	@Column(name = "accountCategory_id")
	private Integer accountCategoryId;

	@Column(name = "NDP")
	private BigDecimal ndp;

	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "vendor_id")
	private Integer vendorId;

	@Column(name = "brand_id")
	private Integer brandId;

	@Column(name = "ABC")
	private String abc;

	@Column(name = "FMS")
	private String fms;

	@Column(name = "AverageConsumption")
	private BigDecimal averageConsumption;

	@Column(name = "MinLevelQty")
	private BigDecimal minLevelQty;

	@Column(name = "MaxLevelQty")
	private BigDecimal MaxLevelQty;

	@Column(name = "ReorderLevelQty")
	private BigDecimal reorderLevelQty;

	@Column(name = "LastSOQRunDate")
	private Date lastSOQRunDate;

	@Column(name = "movementclass_id")
	private Integer movementclassId;

	@Column(name = "LastMvmtRunDate")
	private Date lastMvmtRunDate;

	@Column(name = "SafetyStockQty")
	private BigDecimal safetyStockQty;

	@Column(name = "WorkshopReserveQty")
	private BigDecimal workshopReserveQty;

	@Column(name = "StockAmount")
	private BigDecimal stockAmount;

	@Column(name = "InTransitQty")
	private BigDecimal inTransitQty;

	@Column(name = "OnHandQty")
	private BigDecimal onHandQty;

	@Column(name = "OnOrderQty")
	private BigDecimal onOrderQty;

	@Column(name = "CustOrderQty")
	private BigDecimal custOrderQty;

	@Column(name = "PendingIndentRecvQty")
	private BigDecimal pendingIndentRecvQty;

	@Column(name = "PendingIndentIssQty")
	private BigDecimal pendingIndentIssQty;

	@Column(name = "IsSeasonal")
	@Type(type = "yes_no")
	private Boolean isSeasonal;

	@Column(name = "SeasonFrom")
	@Temporal(TemporalType.DATE)
	private Date seasonFrom;

	@Column(name = "SeasonTo")
	@Temporal(TemporalType.DATE)
	private Date seasonTo;

	@Column(name = "LastLandedCost")
	private BigDecimal lstLandedCost;

	@Column(name = "PurchasePrice")
	private BigDecimal purchasePrice;

	@Column(name = "SellingPrice")
	private BigDecimal sellingPrice;

	@Column(name = "MRP")
	private BigDecimal MRP;

	@Column(name = "IsAuctionable")
	@Type(type = "yes_no")
	private Boolean auctionable;

	@Column(name = "AuctionDate")
	private Date auctionDate;

	@Column(name = "AuctionRemarks")
	private String auctionRemarks;

	@Column(name = "WIP_QTY")
	private BigDecimal wipQty;

	@Column(name = "ALW_PRE_APRVL_IND")
	@Type(type = "yes_no")
	private Boolean alwPreAprvlInd;

	@Column(name = "WARNTY_RATE")
	private BigDecimal warrantyRate;

	@Column(name = "WARNTY_TAX_CATGRY_CD")
	private String WARNTY_TAX_CATGRY_CD;

	@Column(name = "LastTransactionDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTransactionDate;

	@Column(name = "LastUpdateDate")
	private Date LastUpdateDate;

	@Column(name = "Remarks")
	private String remarks;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modfiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "MinOrderQty")
	private Double minOrderQty;

	@Column(name = "PackQty")
	private Double packQty;

	@Column(name = "issueindicator_id")
	private Integer issueIndicatorId;

	@Column(name = "Auctionable_Part_Rate")
	private BigDecimal auctionablePartRate;

	@Column(name = "Auctionable_Part")
	private String auctionablePart;

	@Column(name = "Auctionable_Qty")
	private BigDecimal auctionableQty;

	@Column(name = "LockForTranscation")
	@Type(type = "yes_no")
	private Boolean lockForTranscationStatus;

	@Transient
	private List<PartStockBinEntity> partStockBinEntityList;

	@Transient
	private AdminPartMasterEntity adminPartMasterEntity;

	@Transient
	private Integer taxctgryBranchId;
	
	private transient BigDecimal backOrderQty;

	@Transient
	private String accountCategoryCode;

	@Transient
	private BigDecimal workShopWIPQty;

	@Transient
	private BigDecimal deliveryChallanReserveQty;

	@Transient
	private String taxControlCode;
	@Transient
	private String cgst;
	@Transient
	private String sgst;
	@Transient
	private String igst;

	@Column(name = "VOR_Qty")
	private Double vorQty;
}
