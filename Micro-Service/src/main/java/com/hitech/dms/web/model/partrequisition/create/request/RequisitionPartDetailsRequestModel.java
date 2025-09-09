package com.hitech.dms.web.model.partrequisition.create.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class RequisitionPartDetailsRequestModel {

	private String partId;
	private BigInteger roId;
}
