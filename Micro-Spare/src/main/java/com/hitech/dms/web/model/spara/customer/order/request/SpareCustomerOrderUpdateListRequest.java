package com.hitech.dms.web.model.spara.customer.order.request;

import java.util.List;

import lombok.Data;

@Data
public class SpareCustomerOrderUpdateListRequest {
	
	
	private List<SpareCustomerOrderUpdateRequest> partDetailList;

}
