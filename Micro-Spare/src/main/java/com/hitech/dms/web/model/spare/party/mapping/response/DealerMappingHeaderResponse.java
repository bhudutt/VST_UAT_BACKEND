package com.hitech.dms.web.model.spare.party.mapping.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerMappingHeaderResponse {
	private BigInteger valueId;
	private String valueCode;
	private Integer displayValue;
}
