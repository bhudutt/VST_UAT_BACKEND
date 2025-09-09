package com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload;

import lombok.Data;

@Data
public class DeadStockSearchRequest {
	
	private Integer partId;
	
	private Integer page;
	
	private Integer size;

}
