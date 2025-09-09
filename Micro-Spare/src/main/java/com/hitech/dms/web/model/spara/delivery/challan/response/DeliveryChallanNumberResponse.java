package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class DeliveryChallanNumberResponse {
	
    private BigInteger id;
	
	private String deliveryChallanNumber;
	
	private Date dcDate;

}
