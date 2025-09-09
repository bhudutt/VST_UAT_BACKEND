package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareGrnPONumberReponse {

	private String poNumber;
	private BigInteger poHdrId;
}
