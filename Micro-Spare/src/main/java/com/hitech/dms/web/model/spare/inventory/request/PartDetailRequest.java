package com.hitech.dms.web.model.spare.inventory.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PartDetailRequest {

	private BigInteger grnDtlId;
	private BigInteger partBranchId;
	private BigInteger partId;
	private Integer branchStoreId;
	private Integer availableQty;
	private BigDecimal basicUnitPrice;
	private BigDecimal restrictedQty;
	private BigDecimal unRestrictedQty;
	private Integer branchId;
	private BigInteger stockBinId;
	private BigDecimal claimQty;
	private BigDecimal claimValue;
	private String remarks;
}
