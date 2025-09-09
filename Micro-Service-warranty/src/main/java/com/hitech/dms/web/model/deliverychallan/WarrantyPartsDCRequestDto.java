package com.hitech.dms.web.model.deliverychallan;

import lombok.Data;

@Data
public class WarrantyPartsDCRequestDto {
	
	private String dcNo;
	
	private String lrNo;
	
	private String transporterName;
	
	private String fromDate;
	
	private String toDate;
	
	private int page;
	
	private int size;

}
