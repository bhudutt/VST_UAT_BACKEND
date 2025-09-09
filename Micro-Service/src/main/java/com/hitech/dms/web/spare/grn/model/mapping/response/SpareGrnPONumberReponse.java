package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareGrnPONumberReponse {

	private String poNumber;
	private BigInteger poHdrId;
}
