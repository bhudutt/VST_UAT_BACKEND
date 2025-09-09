package com.hitech.dms.web.model.service.report.request;

import java.util.Date;

import lombok.Data;
@Data
public class HistoryCardResponse {

	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
    private String dateOfVstInvoice;
    private String chassisNumber;
    private String engineNumber;
    private String vinNo;
    private String modelNo;
    private String modelDesc;
    private String dateOfDelivery;
    private String customerName;
    private String customerAddress;
    private String customerMobileNo1;
    private String customerMobileNo2;
    private Integer pageNo;
    private Integer pageSize;
	
}
