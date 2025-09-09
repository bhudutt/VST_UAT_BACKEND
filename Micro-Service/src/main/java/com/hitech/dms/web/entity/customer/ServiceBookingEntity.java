package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Data
@Table(name="SV_Booking")
@Entity
public class ServiceBookingEntity implements Serializable {
	
	private static final long serialVersionUID = -2064280492345441767L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private BigInteger id;
	@JsonProperty(value = "branch", required = true)
	@Column(name="branch_id")
	private BigInteger branch;
	@JsonProperty(value ="servicebookingno")
	@Column(name="bookingno", updatable = false)
	private String servicebookingno;

	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value ="bookingdate")
	@Column(name="booking_date",  updatable = false)
	private Date bookingdate=new Date();
	@JsonProperty(value ="bookingstatus")
	@Column(name="status")
	private String bookingstatus;
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value ="callDate")
	@Column(name="callDate",  updatable = false)
	private Date callDate =new Date();
	@JsonProperty(value ="bookingsource")
	@Column(name="source_of_booking_id")
	private Integer bookingsource;
	@JsonProperty(value ="customerMasterId")
	@Column(name="customer_master_id")
	private BigInteger customerMasterId;
	@JsonProperty(value ="vinno")
	@Column(name="vin_id")
	private BigInteger vinno;
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value ="appointmentdate")
	@Column(name="appointment_date", updatable = false)
	private Date appointmentdate=new Date();
	
	@JsonProperty(value ="appointmenttime")
	@Column(name="appointment_time", updatable = false)
	private String appointmenttime;
	@JsonProperty(value ="servicecategory")
	@Column(name="service_category_id")
	private BigInteger servicecategory;
	@JsonProperty(value ="servicetype")
	@Column(name="service_type_id")
	private BigInteger servicetype;
	@JsonProperty(value ="repairordertype")
	@Column(name="service_repair_type_id")
	private BigInteger repairordertype;
	@JsonProperty(value ="servicebookingremark")
	@Column(name="remarks")
	private String servicebookingremark;
	@JsonProperty(value ="hours")
	@Column(name="hours")
	private Integer hours;
	@JsonProperty(value ="currenthour")
	@Column(name="current_hour")
	private Float currenthour;
	@JsonProperty(value ="previoushour")
	@Column(name="previous_hour")
	private Float previoushour;
	@JsonProperty(value ="totalhour")
	@Column(name="total_hour")
	private Float totalhour;
	@JsonProperty(value ="activitytypeid")
	@Column(name="activity_type_id")
	private BigInteger activitytypeid;
	@JsonProperty(value ="serviceactivityproposalid")
	@Column(name="service_activity_proposal_id")
	private BigInteger serviceactivityproposalid;
	@JsonProperty(value ="mechanicid")
	@Column(name="mechanic_id")
	private BigInteger mechanicid;
	@JsonProperty(value ="placeofserviceid")
	@Column(name="place_of_service_id")
	private BigInteger placeofserviceid;
	@JsonProperty(value ="draftflag")
	@Column(name="draft_flag")
	private byte draftflag;
	@JsonProperty(value ="cancelbookingflag")
	@Column(name="cancel_booking_flag")
	private byte cancelbookingflag;
	@JsonProperty(value ="reasonforcancellation")
	@Column(name="reason_for_cancellation")
	private String reasonforcancellation;
	@JsonProperty(value ="createdby")
	@Column(name="created_by")
	private BigInteger createdby;
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value ="createdon")
	@Column(name="created_on", updatable = false)
	private Date createdon=new Date();
	@JsonProperty(value ="lastmodifiedby")
	@Column(name="last_modified_by")
	private BigInteger lastmodifiedby;
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value ="lastmodifiedon")
	@Column(name="last_modified_on",updatable = false)
	private Date lastmodifiedon=new Date();
	
	
}
