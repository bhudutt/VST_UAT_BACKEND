package com.hitech.dms.web.model.partmaster.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartMasterCreateResponseModel {

	private BigInteger partMasterId;
	private String partMasterNumber;
	private String msg;
	private Integer statusCode;
}
