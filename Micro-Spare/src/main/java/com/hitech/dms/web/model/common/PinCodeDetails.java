package com.hitech.dms.web.model.common;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PinCodeDetails {

	private BigInteger pinId;
	private String pinCode;
	private String postOffice;
	private String city;
	private String tehsil;
	private String district;
	private String state;
}
