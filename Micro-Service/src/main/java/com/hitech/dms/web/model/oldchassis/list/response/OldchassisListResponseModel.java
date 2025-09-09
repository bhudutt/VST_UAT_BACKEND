package com.hitech.dms.web.model.oldchassis.list.response;


import java.util.Date;

import lombok.Data;

@Data
public class OldchassisListResponseModel {

	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String registrationNumber;
	private Date saleDate= new Date();
	private String createdBy;
	private Date createdDate= new Date();
    private String mobileNo;
    private String customerName;
    private String whatsAppNo;
    private String alternateNo;
    private String emailId;
    private String panNo;
    private String aadharCardNo;
    private String address1;
    private String address2;
    private String address3;
    private Integer customerCategory;
    private String customerCode;
    private String prospectType;
    
	
}
