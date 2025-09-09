package com.hitech.dms.web.model.partmaster.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ServiceQutationCreateResponseModel {

	private Integer quotationId;
	private String quotationNumber;
	private String msg;
	private Integer statusCode;
}
