package com.hitech.dms.web.model.deliverychallan;

import lombok.Data;

@Data
public class WcrDispatchRequestDto {

	private String wcrType;
	
	private String fromDate;
	
	private String toDate;
	
	private int page;
	
	private int size;
	
}
