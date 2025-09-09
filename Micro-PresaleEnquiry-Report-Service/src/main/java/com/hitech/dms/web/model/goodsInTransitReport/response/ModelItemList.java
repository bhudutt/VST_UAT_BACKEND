package com.hitech.dms.web.model.goodsInTransitReport.response;

import java.math.BigInteger;

import lombok.Data;
@Data
public class ModelItemList {
	
	private BigInteger machineItemId;
	
	private BigInteger modelId;
	
	private String itemNo;
	
	private String itemDesc;
	
	private String variant;

}
