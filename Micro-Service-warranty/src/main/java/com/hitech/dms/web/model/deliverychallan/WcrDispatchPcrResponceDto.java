package com.hitech.dms.web.model.deliverychallan;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class WcrDispatchPcrResponceDto {
	
	private BigInteger wcrId;
	private String wcrNo;
	private Date wcrDate;
	private String jobCardNo;
	private Date jobCardDate;
	private String pcrNo;
	private Date pcrDate;
	private String model;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private BigInteger hour;
	private String wcrType;
	private BigInteger noOfTime;
	private BigInteger claimValue;
	
}
