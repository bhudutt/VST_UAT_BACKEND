package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SparePartRequisitionResponseModel {

	private String msg;
	private Integer statusCode;
	private BigInteger requisitionId;
	private String  requisitionNumber;
	
}
