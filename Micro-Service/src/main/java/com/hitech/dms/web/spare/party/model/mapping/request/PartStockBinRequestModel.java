package com.hitech.dms.web.spare.party.model.mapping.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PartStockBinRequestModel {

	
	private BigInteger stockBinId;
	private BigInteger partBranchId;
	private String isDefault;
	private String isActive;
	private String binName;
	private String binType;
	private BigDecimal currentStock;

	
}
