package com.hitech.dms.web.model.spare.branchTransfer.receipt;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class IndentNumberDetails {

	private BigInteger paIndDtlId;
	private Integer partId;
	private String partNumber;
	private String partdesc;
	private Integer totalStock;
	private BigDecimal totalValue;
	private Integer indentQty;
	private Integer issueQty;
	private BigDecimal basicUnitPrice;
	private String fromStore;
	private String storeBinLocation;
	private String binStock;
}
