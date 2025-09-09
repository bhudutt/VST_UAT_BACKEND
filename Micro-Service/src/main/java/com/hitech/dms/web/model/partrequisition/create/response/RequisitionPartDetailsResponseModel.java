package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class RequisitionPartDetailsResponseModel {
	
	
	private String partNo;
	private String partDesc;
	private String uom;
	private Integer currentStock;
	private BigDecimal marp;
	private Integer partBranchId;
	//private BigDecimal requisitionQty;
	private BigDecimal issuedQty;
	private BigDecimal pendingQty;
	private String msg;
	
}
