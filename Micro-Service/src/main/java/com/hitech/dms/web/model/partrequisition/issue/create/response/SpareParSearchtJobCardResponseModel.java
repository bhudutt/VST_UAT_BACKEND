package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareParSearchtJobCardResponseModel {

	private BigInteger jobCardId;
	private String jobCardNumber;
	private BigInteger requisitionId;
	private String  requisitionNo;
	private String status;
	private String requisitionStatus;
	
}
