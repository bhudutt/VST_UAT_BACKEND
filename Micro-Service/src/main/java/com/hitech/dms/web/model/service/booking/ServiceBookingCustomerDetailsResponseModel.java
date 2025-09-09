package com.hitech.dms.web.model.service.booking;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceBookingCustomerDetailsResponseModel {

	private String customecode;
	private String customername;
	private String mobileno;
	private String address;
	private String city;
	private String previousservicetype;
	private Date previousservicedate;
	private Time previousservicehour;
	private String nextdueservicetype;
}
