package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ServiceBookingServiceTypeListRequestModel {

	@Column(name="Service_Type_ID")
	private BigInteger serviceTypeID;
	@Column(name="SrvTypeDesc")
	private String serviceTypeDescription;
	
}
