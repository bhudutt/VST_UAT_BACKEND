package com.hitech.dms.web.model.spara.customer.order.response;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerOrderNumberResponse {

	private Integer id;

	private String customerOrderNumber;

	private Date customerOrderDate;
	
	private String msg;
	
	private Integer statusCode;

}
