package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class OldChassisFetchDTLResponseModel {
    
	private BigInteger machineItemId;
	private String chassisNo;
	private String vinNo;
	private String registerationNumber;
	private Date saleDate=new Date();
	private String engineNo;
	private String segmentName;
	private String seriesName;
	private String modelName;
	private String profitDesc;
	private String variant;
	private String itemNo;
	private String itemDesc;
	private String mobileNo;
	private String firstName;
	private String middleName;
	private String lastName;
	private String whatsappNo;
	private String alternateNo;
	private String panNo;
	private String emailId;
	private String aadharCardNo;
	private String address1;
	private String address2;
	private String address3;
	
}
