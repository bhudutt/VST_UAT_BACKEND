package com.hitech.dms.web.model.partreturn.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;


import lombok.Data;
@Data
public class PartReturnIssueDetailsResponseModel {
	
	private BigInteger issueDtlId;
	private BigInteger stockBinId;
	private Integer partBranchId;
	private Integer branchStoreId;
	private BigInteger issueId;
	private String issuenumber;
	private String partNumber;
	private String partdesc;
	private BigDecimal pendingQty;
	private BigDecimal requestQty;
	private BigDecimal issuedqty;
	private String fromstore;
	private String binlocation;
	private String returnRemark;
	private BigDecimal unitPrice;
	private BigInteger stockStoreId;
	private Integer partId;
	private Integer requisitionId;
	
}
