package com.hitech.dms.web.model.partrequisition.search.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SparePartRequisitionViewResponseModel {

	private String requisitionNumber;
    private String requisitionDate;
    private String requisitionType;
    private String jobCardNumber;
    private String jobCardDate;
    private String requestedBy;
    private String remarks;
    private String chassisNumber;
    private String registrationNumber;
    private String modelVariant;
    private String customerName;
    private String partNumber;
    private String partDescription;
    private String uomDescription;
    private Integer currentStock;
    private BigDecimal mrp;
    private BigDecimal requisitionQty;
    private BigDecimal issuedQty;
    private BigDecimal pendingQty;
    private Integer partBranchId;
}
