package com.hitech.dms.web.partsStock.Model;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class StockBinDetailResponseModel {
	
	
	private int statusCode;
	
	private String message;
	private List<StockBinDetailModel> stockBinDtlList;
	
	

}
