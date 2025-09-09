package com.hitech.dms.web.model.spara.customer.order.request;


import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PartyCodeCreateRequestModel {

	private BigInteger branchId;
	
	private String searchText;
	
	private BigInteger partyType;
	

}
