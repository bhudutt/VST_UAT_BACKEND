package com.hitech.dms.web.model.baymaster.responselist;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BayTypeModel {
	private BigInteger valueId;
	private String valueCode;
	private Integer displayValue;
}
