package com.hitech.dms.web.model.vehicle.master;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class VehicleMasterSearchListResponseModel {
	
	private String chassisNo;
    private String engineNo;
    private String vinNo;
    private String mfgInvoiceNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date mfgInvoiceDate;
    private String profitCenter;
    private String model;
    private String series;
    private String segment;
    private String variant;
    private String itemNo;
    private String itemDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date saleDate;
    private String registrationNo;
	private String customerName;
	private String customerMobile;
	private String customerCode;
	
}

