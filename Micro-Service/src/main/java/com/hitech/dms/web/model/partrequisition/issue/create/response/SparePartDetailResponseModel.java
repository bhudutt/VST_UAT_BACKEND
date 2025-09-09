package com.hitech.dms.web.model.partrequisition.issue.create.response;
 
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class SparePartDetailResponseModel {

	private String partRequisitionNo;
	private String partNo;
	private String partDesc;
	private BigDecimal pendingReQty;
	private BigDecimal requestedQty;
	private BigDecimal issuedQty;
	private Integer totalBranchStock;
	//private BigDecimal issueQty;
	private BigDecimal mrp;
	//private BigDecimal currentStock;
	private String fromStore;
//	private Integer binLocation;
	private String binLocationName;
	private BigInteger stockBinid;
	private Integer refpartbranchId;
	private BigInteger requisitionId;
	private Boolean binFlag;
	
}
