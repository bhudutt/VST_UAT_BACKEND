package com.hitech.dms.web.model.spara.customer.order.response;

import java.util.List;

import lombok.Data;

@Data
public class PartyCodeListResponseModel 
{
	
		private List<PartyCodeModel> partyCode;
		private Integer recordCount;
}
