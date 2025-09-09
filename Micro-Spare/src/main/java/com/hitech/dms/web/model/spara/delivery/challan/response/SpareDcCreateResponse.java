package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareDcCreateResponse {

	private BigInteger deliveryId;
	private String deliveryChallanNumber;
	private String msg;
	private Integer statusCode;
}
