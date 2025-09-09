package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OldChassisNumberResponseModel {

	private BigInteger vinId;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String registrationNumber;
	private String itemDescription;
	private BigInteger machineItemId;
	private String status;
	private String bookingNo;
	
	
}
