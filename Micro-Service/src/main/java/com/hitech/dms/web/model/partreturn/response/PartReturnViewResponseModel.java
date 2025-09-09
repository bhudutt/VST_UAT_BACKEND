package com.hitech.dms.web.model.partreturn.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartReturnViewResponseModel {

	private String returnType;
    private String returnNumber;
    private String returnDate;
    private String roNumber;
    private String IssueNumber;
    private String IssueDate;
    private String openingDate;
    private String returnBy;
    private String delayReasonDesc;
    private String requisitionNumber;
    private String requisitionDate;
    private String chassisNumber;
    private String registrationNumber;
    private String modelVariant;
    private String customerName;
    private String partNumber;
    private String partDesc;
    private BigDecimal pendingRequestedQty;
    private BigDecimal requestedQty;
    private BigDecimal issuedQty;
    private BigDecimal returnedQty;
    private String storeDesc;
    private String binLocation;
    private String remarks;

}
