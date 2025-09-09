package com.hitech.dms.web.model.storemaster.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreMasterCreateResponseModel {

	private BigInteger storeId;
	private String storeNumber;
	private String msg;
	private Integer statusCode;
}
