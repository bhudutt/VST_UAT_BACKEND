package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BinStoreList {
	
	private BigInteger partId;
	
	private Integer binId;
	
	private String BinLocation;
	
	private String fromStore;

}
