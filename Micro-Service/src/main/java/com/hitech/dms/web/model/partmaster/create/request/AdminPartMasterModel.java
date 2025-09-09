package com.hitech.dms.web.model.partmaster.create.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AdminPartMasterModel {
	private BigDecimal mrp;
	private BigDecimal dealerNdp;
	private BigDecimal distributorNdp;
	Map<String, String> errorList;
	//private List<SAPPartMasterStgEntity> partStgList;
	private MultipartFile partMasterFile;
	private String xmlPath;
	private String formAction;
	private Integer id;
	private String mfg_code;
	private String partNumber;
	private String partDescription;
	private Integer uomId;
	private String uomDesc;
	private Double minOrderQty;
	private String alternativePartNo;
	private String oemPartIndicator = "L";
	private Integer partproductdivisionId;
	private Double packQty;
	private Integer materialPricingGroupId;
	private Double sellingPrice;
	private Double perVehicleQty;
	private String salesOrg;
	private Boolean mfgPartNoStatus = false;
	private String materialGroup;
	private String taxControlCode;
	private Boolean allowDecimal = true;
	private Integer issueIndicatorId;
	private Integer partCategoryId;
	private String targetSettingGroup;

	private Boolean fromSAP = false;
	private Boolean orderToOEM = false;
	private String oemequivalentPartNumber;

	private String partTypeId;
	private Double xmlMRP;
	private Double xml_GNDP_PRICE;
	private Integer xml_MANDT;
	private Integer xml_VTWEG;
	private String xml_DATBI;
	private String xml_DATAB;
	private String xml_STATUS;

	private String xml_SESSIONID;
	private String xml_MSTAV;
	private String xml_PROVG;
	private Integer xml_TAXKM;
	private String xml_DOCTYPE;

	private Double xml_SCMNG;
	private Date createdDate;
	private String createdBy;
	private Date modfiedDate;
	private String modifiedBy;
	private Date lastUpdatedOn;
	private Boolean creditCheckRequiredFlag;
	private Boolean mrpChangedAllowedFlag;
	private Boolean partUnderWarrantyStatus;
	private Boolean partSearchFlag;

	private Double branchPartMrp;
	private String hsinCode;
	// private Double taxRate;
	private String controlCode;
	private List<PartControlModel> partControlList;

	private Integer modelGroupId;
	private Integer modelVariantId;
	private Integer aggregateId;
	private Integer modelPlateformId;
	private String aggregate;

	private Boolean activeFlag;
	private Double vorQty;

	private String ved;
	private String bharatStage;
}
