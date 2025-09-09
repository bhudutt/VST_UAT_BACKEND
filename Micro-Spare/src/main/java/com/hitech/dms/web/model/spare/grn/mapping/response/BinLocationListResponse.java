package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BinLocationListResponse {

	private BigInteger stockBinId;
	private String binName;
	
}
