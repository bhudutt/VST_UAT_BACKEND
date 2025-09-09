package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OldChassisMachineDTLByChassisRequestModel {
    
	private String userCode;
	private String chassisNo;
	private String isFor;
	private BigInteger vinID;

}
