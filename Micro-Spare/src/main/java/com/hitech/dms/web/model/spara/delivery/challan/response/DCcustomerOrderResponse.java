package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.util.Date;

import lombok.Data;

@Data
public class DCcustomerOrderResponse {
	
	private Integer customerId;
	
	private String customerOrderNumber;
	
	private Date customerOrderDate;
	
	private char dcSelect;

}
