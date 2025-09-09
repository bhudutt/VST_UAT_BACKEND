package com.hitech.dms.web.spare.party.model.mapping.request;

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


import lombok.Data;


@Data
public class PartTableRequestModel {
	
	private int partId;
	private String partNumber;
	private String partDesc;
	private int uomId;
	private String altPartNumber;
	private int issueIndiacatorId;
	private int partCategoryId;
	private String allowDecimalQty;
	private int minOrderQty;
	private int aggregateId;
	private int modelGroupId;
	private int modelPlatformId;
	private int perVehicleQuantity;
	private int packQty;
	private String orderToOem;
	private int modelVariantId;
	private String isPartWarranty;
	private String isPartMfgNo;
	private String hssnCode;
	private BigInteger iGst;
	private BigInteger cGst;
	private BigInteger sGst;

	
	
	
	
	

}
