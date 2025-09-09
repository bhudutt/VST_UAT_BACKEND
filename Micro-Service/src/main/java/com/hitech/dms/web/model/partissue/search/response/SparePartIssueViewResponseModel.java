package com.hitech.dms.web.model.partissue.search.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SparePartIssueViewResponseModel {
	
	
    private String issueType;
    private String issueNumber;
    private String issueDate;
    private String roNumber;
    private String openingDate;
    private String issueBy;
    private String remarks;
    private String chassisNumber;
    private String registrationNumber;
    private String modelVariant;
    private String customerName;
    private String requisitionNo;
    private String requisitionDates;

}
