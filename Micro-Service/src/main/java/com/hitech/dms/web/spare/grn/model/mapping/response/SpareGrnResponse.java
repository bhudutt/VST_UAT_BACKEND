package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareGrnResponse {

	private String grnNumber;
	private int statusCode;
	private String msg;
}
