package com.hitech.dms.web.model.spara.customer.order.response;

import java.util.List;

import lombok.Data;

@Data
public class PartyCodeSearchMainResponseModel 
{
	
		private List<CustomerOrderPartyCodeSearchResponseModel> searchList;
		private Integer recordCount;
}
