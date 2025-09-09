package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;

import lombok.Data;
@Data
@Entity
public class ServiceBookingRequestModel {

	private String branch;
	private BigInteger servicebookingno;
	private Date bookingdate;
	private boolean bookingstatus;
	private Date bookingdatetime;
	private String bookingsource;
	private String customermobileno;
	private String customername;
	private String chassisno;
	private String engineno;
	private String modeldescription;
	private String vinno;
	private String registrationno;
	private Date appointmentdate;
	private Time appointmenttime; 
	private String servicecategory;
	private String servicetype;
	private String repairordertype;
	private String servicebookingremark;
	
}
