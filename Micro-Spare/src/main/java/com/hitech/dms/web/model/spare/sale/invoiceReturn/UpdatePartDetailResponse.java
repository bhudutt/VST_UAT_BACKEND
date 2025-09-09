package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class UpdatePartDetailResponse {
	
	private Integer partBranchId;
	private BigInteger stockBinId;
	private BigDecimal recieptQuantity;
	private BigDecimal usedQuantity;
	private BigDecimal AvlbQty;
	

}
