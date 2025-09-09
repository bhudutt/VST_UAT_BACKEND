package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ServiceBookingStatusRequestModel {

	private BigInteger id;
	@Column(name="active_status")
	private String activestatus;
	private String status;
}
