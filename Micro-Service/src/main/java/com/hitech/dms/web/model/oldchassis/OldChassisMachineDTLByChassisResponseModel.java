package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OldChassisMachineDTLByChassisResponseModel {

	private BigInteger vinId;
	private String displayValue;
	private String ChassisNo;
	private String errorFlag;
	private String errorMsg;
	
}
