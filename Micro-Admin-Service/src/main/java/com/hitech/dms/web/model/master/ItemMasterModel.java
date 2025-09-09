package com.hitech.dms.web.model.master;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ItemMasterModel {

	private String itemNo; 
	private String itemDescription; 
	private String productGroup;
	private String modelName; 
	private String variant; 
	private String profitCente;
	private String productDivision; 
	private String seriesName; 
	private String segmentName;
	private char isChasisNoReq; 
	private char isEngineNoReq;
	private char isActive; 
	private String hsnCode; 
	private Double gst;
}
