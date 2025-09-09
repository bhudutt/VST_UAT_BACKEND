package com.hitech.dms.web.model.registrationcertificate.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCreateResponseModel {

	private BigInteger vinId;
	private String chassisNumber;
	private String msg;
	private Integer statusCode;
	
}
