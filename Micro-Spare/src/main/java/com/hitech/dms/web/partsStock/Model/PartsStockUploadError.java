package com.hitech.dms.web.partsStock.Model;

import lombok.Data;

@Data
public class PartsStockUploadError {
	
	private String partNo;
	private String partRemarks;
	private String storeCode;
	private String storeRemarks;

}
