package com.hitech.dms.web.model.spare.branchTransfer.issue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;

import lombok.Data;

@Data
public class IndentNumberDetails {

	private BigInteger paIndDtlId;
	private Integer partBranchId;
	private Integer branchStoreId;
	private Integer partId;
	private String partNumber;
	private String partdesc;
	private Integer totalStock;
	private BigDecimal totalValue;
	private Integer indentQty;
	private Integer issueQty;
	private Integer toStore; //branchStoreId
	private BigDecimal basicUnitPrice;
	private String fromStore;
	private String storeBinLocation;
	private String binStock;
	private Integer binLenght;
	private boolean binFlag;
	private String flag;
	private BigInteger binId;
	private List<BranchSpareIssueBinStockResponse> binRequest;
}
