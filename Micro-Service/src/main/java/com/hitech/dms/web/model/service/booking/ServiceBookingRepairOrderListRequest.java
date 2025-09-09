package com.hitech.dms.web.model.service.booking;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ServiceBookingRepairOrderListRequest {

	private Integer id;
	@Column(name="repair_order_desc")
	private String repairOrderDesc;
}
