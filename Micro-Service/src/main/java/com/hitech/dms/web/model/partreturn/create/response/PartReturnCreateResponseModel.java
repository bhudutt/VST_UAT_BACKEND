package com.hitech.dms.web.model.partreturn.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartReturnCreateResponseModel {

	private String msg;
	private Integer statusCode;
	private BigInteger returnId;
	private String returnNumber;
}
