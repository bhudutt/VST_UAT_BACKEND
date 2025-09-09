package com.hitech.dms.web.model.spara.delivery.challan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinDetailRequest {
	
	private String storeName;
	
	private String binLocation;
	
	private Integer availableStock;
	
	private Integer unitPrice;
	
	private Integer issueQty;
	
	private Integer binId;
	
	private Integer partId;
	
	private Integer branchStoreId;
	
	private Integer stockStoreId;
	
}
