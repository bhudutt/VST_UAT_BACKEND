package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import com.hitech.dms.web.model.partmaster.create.request.PartStatePriceModel;

import lombok.Data;

@Table(name = "PA_PART")
@Entity
@Data
public class AdminPartMasterEntity {

	@Column(name = "MRP", updatable = false)
	private BigDecimal mrp;

	@Column(name = "DEALER_NDP", updatable = false)
	private BigDecimal dealerNdp;

	@Column(name = "DIS_NDP", updatable = false)
	private BigDecimal distributorNdp;

	@Column(name = "IsFromSAP")
	@Type(type = "yes_no")
	private Boolean fromSapFlag;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "part_id")
	private Integer id;

	@Column(name = "PartNumber")
	private String partNumber;

	@Column(name = "PartDesc")
	private String partDescription;

	@Column(name = "uom_id")
	private Integer uomId;

	@Column(name = "AltPartNumber")
	private String alternativePartNo;

	@Column(name = "OEMPartIndicator")
	private String oemPartIndicator;

	@Column(name = "partproductdivision_id")
	private Integer partproductdivisionId;

	@Column(name = "PackQty")
	private Double packQty;

	@Column(name = "PerVehicleQty")
	private Double perVehicleQty;

	@Column(name = "Is_MFG_PartNo")
	@Type(type = "yes_no")
	private Boolean mfgPartNoStatus;

	@Column(name = "AllowDecimalInQty")
	@Type(type = "yes_no")
	private Boolean allowDecimal;

	@Column(name = "issueindicator_id")
	private Integer issueIndicatorId;

	@Column(name = "partcategory_id")
	private Integer partCategoryId;

	@Column(name = "OrderToOEM")
	@Type(type = "yes_no")
	private Boolean orderToOEM;

	@Column(name = "OEMequivalentPartNumber")
	private String oemequivalentPartNumber;

	@Column(name = "IsPartUnderWarranty")
	@Type(type = "yes_no")
	private Boolean partUnderWarrantyStatus;

	@Column(name = "HSN_CODE", updatable = false)
	private String controlCode;

	@Column(name = "MinOrderQty")
	private Double minOrderQty;

	@Column(name = "Model_Group_Id")
	private Integer modelGroupId;

	@Column(name = "MODEL_PLATFORM_ID")
	private Integer modelPlateformId;

	@Column(name = "Model_Variant_Id")
	private Integer modelVariantId;

	@Column(name = "AGGREGATE_ID")
	private Integer aggregateId;

	@Column(name = "SalesOrg")
	private String salesOrg;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;
	
	@Column(name = "CGST", updatable = false)
	private BigInteger cGst;
	
	@Column(name = "IGST", updatable = false)
	private BigInteger iGst;
	
	@Column(name = "SGST", updatable = false)
	private BigInteger sGst;
	
	
	
	

	@Column(name = "ModifiedDate")
	private Date modfiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "LastUpdatedOn")
	private Date lastUpdatedOn;

	@Version
	@Column(name = "VersionNo")
	private Integer versionNo;

	@Transient
	private String partTypeId;

	@Transient
	private Double purchasePrice;

	@Transient
	private BigDecimal locationStock;
	@Transient
	private Double backOrderQty;
	@Transient
	private BigDecimal branchPartMrp;
	@Transient
	private Integer makeId;
	@Transient
	private Integer modelId;
	@Transient
	private String makeDesc;
	@Transient
	private String modelDesc;

	/*
	 * @Column(name = "VOR_QTY") private Double vorQty;
	 */

	private transient List<PartStatePriceModel> partStatePriceList;

	private transient List<PartControlEntity> partControlList;

	private transient Double[] taxRate;
}
