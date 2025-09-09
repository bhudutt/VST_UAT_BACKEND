package com.hitech.dms.web.model.pcr;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceHistoryDto {

	private String typeOfService;
	
	private BigInteger hour;
	
	private String jobcardNo;
	
	private Date jobcardDate;
	
	private String pcrNo;
	
	private Date pcrDate;
}
