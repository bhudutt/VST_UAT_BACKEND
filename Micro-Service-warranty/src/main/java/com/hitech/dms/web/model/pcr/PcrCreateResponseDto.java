package com.hitech.dms.web.model.pcr;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PcrCreateResponseDto {
	private String msg;
	private Integer statusCode;
	private BigInteger roId;
	private String pcrNo;

}
