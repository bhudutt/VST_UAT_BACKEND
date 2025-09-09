package com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class StockAdjustmentResponse {
	
	private Map<Integer, String> errorPartData;
	
	private List<StockAdjustmentListResponse> adjustmentListResponses = new ArrayList<>();

}
