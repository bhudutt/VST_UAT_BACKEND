package com.hitech.dms.web.model.partmaster.create.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class PartMasterFormRequestModel {

	private String docType;
	private String docName;
	private String docPath;
	private String auctionablePart;
	private BigDecimal auctionableQty;
	private BigDecimal backOrderQty;
	private MultipartFile auctionablePartFile;
	private BigDecimal ndp;
	private String taxCategory;
	private Integer partBranchId;
	private Integer partId;
	private Integer branchId;
	private Integer vendorId;
	private Integer brandId;
	private String accountCategoryCode;
	private Integer taxctgryBranchId;
	private Integer accountCategoryId;
	private String abc;
	private String fms;
	private Double averageConsumption;
	private Double minLevelQty;
	private Double MaxLevelQty;
	private Double reorderLevelQty;
	private Date lastSOQRunDate;
	private Integer movementclassId;
	private Date lastMvmtRunDate;
	private Double safetyStockQty;
	private Double workshopReserveQty;
	private Double stockAmount;
	private Double inTransitQty;
	private BigDecimal onHandQty;
	private Double onOrderQty;
	private Double custOrderQty;
	private Double pendingIndentRecvQty;
	private Double pendingIndentIssQty;
	private Boolean seasonal;
	private String seasonFrom;
	private String seasonTo;
	private Double lstLandedCost;
	private Double purchasePrice;
	private Double sellingPrice;
	private Double MRP;
	private Boolean auctionable;
	private Date auctionDate;
	private String auctionRemarks;
	private BigDecimal wipQty;
	private Boolean alwPreAprvlInd = true;
	private Double warrantyRate;
	private String WARNTY_TAX_CATGRY_CD;
	private Date lastTransactionDate;
	private Date LastUpdateDate;
	private String remarks;
	private Date createdDate;
	private String createdBy;
	private Date modfiedDate;
	private String modifiedBy;
	private Boolean orderToOEM;
	private Boolean cssEnabledMrpChangedAllowed;

	private Double minOrderQty;
	private Double packQty;
	private Integer issueIndicatorId;
	private String errorMsg;
	
	// Optional
		private Integer storeId;
		private Integer binLocationId;
		private PartBranchBinModel partBranchBinModel;
		private List<PartStockBinModel> partStockBinModelList;
		private AdminPartMasterModel adminPartMasterModel;

		private Integer branchStoreIdForJSPView;

		private BigDecimal workShopWIPQty;

		private BigDecimal deliveryChallanReserveQty;

		private String taxControlCode;
		private String cgst;
		private String sgst;
		private String igst;
		
		// Spares
		private String partMake;
		private String partModel;
		private String aggregate;
		private String modelGrp;
		private String modelPlatform;
		private String modelVariant;
		private String hsn;
		private String reOrderQty;
		private String fsn;
		private String ved;
		private String auctionablePartRate;
		private String auctionablePartImage;

		private Double vorQty;
}
