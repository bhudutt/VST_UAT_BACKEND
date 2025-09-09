package com.hitech.dms.web.model.partrequisition.create.response;


import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class RequitionJobCardNoByResponseModel {
   
	private Integer branchId;
	private BigInteger vinId;
	private String modelName;
	private String chassisNo;
	private String RegistrationNumber;
	private String customerName;
	private Date   jobcardDate;
}
