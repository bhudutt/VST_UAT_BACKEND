package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DistrictResponse {

	private BigInteger Id;
	private String districtCode;
	private String districtDesc;
}
