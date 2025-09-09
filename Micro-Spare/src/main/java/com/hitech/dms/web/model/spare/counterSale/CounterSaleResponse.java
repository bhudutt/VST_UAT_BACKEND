package com.hitech.dms.web.model.spare.counterSale;


import java.math.BigInteger;

import lombok.Data;

@Data
public class CounterSaleResponse {

	private BigInteger id;
	private String customerName;
	private String mobileNo;
	private BigInteger pinId;
	private String pinCode;
	private String postOffice;
	private String city;
	private String tehsil;
	private String district;
	private String state;
}
