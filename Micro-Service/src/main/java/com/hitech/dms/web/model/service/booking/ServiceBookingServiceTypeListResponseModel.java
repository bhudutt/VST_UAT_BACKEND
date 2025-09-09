package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ServiceBookingServiceTypeListResponseModel {

	@Column(name="Service_Type_ID")
	private Integer serviceTypeID;
	@Column(name="SrvTypeDesc")
	private String serviceTypeDescription;
}
