package com.hitech.dms.web.model.spare.picklist;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PickListPartDetailResponse {

	private BigInteger pickListDtlId;
	private int partId;
	private int partBranchId;
	private BigInteger stockBinId;
	private Integer branchStoreId;
	private String storeDesc;
	private String partNumber;
	private String partDesc;
	private String binLocation;
	private String hsnCode;
	private BigDecimal mrp;
	private BigDecimal orderQty;
	private BigDecimal balanceQty;
	private BigDecimal issueQty;
	private Integer availableQty;
	
}
