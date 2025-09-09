package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BinLocationListResponse {

	private BigInteger stockBinId;
	private String binName;
	
}
