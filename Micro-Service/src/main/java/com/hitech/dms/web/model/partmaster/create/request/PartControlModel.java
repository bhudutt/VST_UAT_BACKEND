package com.hitech.dms.web.model.partmaster.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PartControlModel {

	private Integer controlCodeId;
	private String controlCode;
	private Integer taxTypeId;
	private String taxTypeDesc;
	private BigInteger sgstRate;
	private BigInteger cgstRate;
	private BigInteger igstRate;
	private BigDecimal taxRate;
	private Boolean isActive;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
}
