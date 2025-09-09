package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class OldChassisMachineDTLByVinIDResponseModel {

	private BigInteger vinId;
	private String chassisNo;
	private String engnieNo;
	private String vinNo;
	private String registrationNo;
	private Date SalDate= new Date();
	private BigInteger profitCenter;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private String itemNo;
	private String itemDesc;
}
