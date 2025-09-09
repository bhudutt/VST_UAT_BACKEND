package com.hitech.dms.web.spare.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SparePartUploadResponseModel {
	
	private BigInteger partMasterId;
	private String partMasterNumber;
	private String msg;
	private Integer statusCode;
}
