package com.hitech.dms.web.model.service.booking;

import javax.persistence.Column;

import lombok.Data;
@Data
public class ServiceBookingSourceListResponseModel {
	
	private Integer id;
	@Column(name="service_source_desc")
	private String serviceSourceDesc;

}
