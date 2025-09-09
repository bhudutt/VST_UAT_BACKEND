package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareGrnResponse {

	private String grnNumber;
	private int statusCode;
	private String msg;
}
