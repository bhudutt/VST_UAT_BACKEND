package com.hitech.dms.web.entity.sales.stock.upload.stg;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Table(name = "STG_STOCK_ADJUSTMENT_UPLOAD")
@Entity
@Data
public class StgStockAdjustmentUpload implements Serializable {

	private static final long serialVersionUID = -8843078251082005422L;

	@Id
	@Column(name = "Stg_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stgId;

	@Column(name = "PC_Id")
	private Integer pcId;
	
	@Column(name = "Dealer_Code")
	private String dealerCode;
	
	@Column(name = "Branch_Code")
	private String branchCode;
	
	@Column(name = "Item_No")
	private String itemNo;
	
	@Column(name = "Item_desc")
	private String itemDesc;
	
	@Column(name = "Model_Name")
	private String modelName;
	
	@Column(name = "Vin_No")
	private String vinNo;
	
	@Column(name = "Chassis_No")
	private String chassisNo;
	
	@Column(name = "Engine_No")
	private String engineNo;
	
	@Column(name = "Selling_dealer_code")
	private String sellingDealerCode;
	
	@Column(name = "Quantity")
	private Integer quantity;
	
	
	@Column(name = "Unit_Price")
	private Double unitPrice;
	
	@Column(name = "MfgInvoiceNumber")
	private String mfgInvoiceNumber;

	@Column(name = "MfgInvoiceDate")
	private String mfgInvoiceDate;
	
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "Dealer_Id")
	private Integer dealederId;
	
	@Column(name = "grn_no")
	private String grnNo;
	
	@Column(name = "plant_code")
	private String planCode;
	
	
}
