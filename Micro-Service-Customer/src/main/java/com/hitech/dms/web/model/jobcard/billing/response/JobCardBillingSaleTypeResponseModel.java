package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class JobCardBillingSaleTypeResponseModel {

	private BigInteger Id;
	private String typeCode;
	private String typeName;
	private Integer displayOrder;
	
}
