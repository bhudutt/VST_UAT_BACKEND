package com.hitech.dms.web.model.pdi.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdiCreateResponseModel {

	private Integer pdiId;
	private String pdiNumber;
	private String msg;
	private Integer statusCode;

}
