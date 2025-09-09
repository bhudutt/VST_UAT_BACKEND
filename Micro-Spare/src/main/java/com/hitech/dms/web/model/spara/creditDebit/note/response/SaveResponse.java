package com.hitech.dms.web.model.spara.creditDebit.note.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SaveResponse {
	
	private BigInteger id;
	private String number;
	private String msg;
	private Integer statusCode;

}
