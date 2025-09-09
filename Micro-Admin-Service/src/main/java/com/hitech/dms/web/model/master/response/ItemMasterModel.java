package com.hitech.dms.web.model.master.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ItemMasterModel {

	private String itemNo; 
	private String itemDescription; 
	private String productGroup;
	private BigInteger modelId;
	private String modelName; 
	private String variant; 
	private BigInteger pcId;
	private String profitCente;
	private BigInteger productDivId;
	private String productDivision; 
	private String seriesName; 
	private String segmentName;
	private char isChasisNoReq; 
	private char isEngineNoReq;
	private char isActive; 
	private String hsnCode; 
	private Double gst;
}
