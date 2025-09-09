package com.hitech.dms.web.model.partissue.search.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SparePartIssuePartDetailResponseModel {

	private String requisitionNumber;
	private String partNumber;
    private String partDescription;
    private BigDecimal pendingQty;
    private BigDecimal requestQty;
    private BigDecimal issuedQty;
    private Integer totalBranchStock;
    private BigDecimal currentStock;
    private BigDecimal mrp;
    private String storeDesc;
    private String binLocation;
}
