package com.hitech.dms.web.model.SpareModel;

import java.math.BigInteger;

import lombok.Data;
@Data
public class SparePoCreateResponseModel {
	private BigInteger poHdrId;
	private String poNumber;
	private String msg;
	private Integer statusCode;
}
