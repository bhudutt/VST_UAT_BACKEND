package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class RequisitionJobCardListResponseModel {
    
	//private String jobCardNo;
	private String RONumber;
	//private Date closingDate;
	private String VehSrNo;
	private BigInteger roId;
	private Date openingDate;
	private String status;
	private String chassisNo;
	private String engineNo;
	private String registrationNumber;
}
