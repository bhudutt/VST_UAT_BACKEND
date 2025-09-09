package com.hitech.dms.web.spare.party.model.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerMappingHeaderResponse {
	private BigInteger valueId;
	private String valueCode;
	private Integer displayValue;
}
