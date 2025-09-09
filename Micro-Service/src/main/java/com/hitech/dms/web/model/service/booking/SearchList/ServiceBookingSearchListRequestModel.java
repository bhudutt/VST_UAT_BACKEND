package com.hitech.dms.web.model.service.booking.SearchList;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Data
public class ServiceBookingSearchListRequestModel {
	
	private String userCode;
	private String fromDate;
	private String toDate;
	private String model;
	private String variant;
	private String bookingNo;
	private String bookingStatus;
	private String chassisNo;
	private String bookingSource;
	private String serviceCategory;
	private String serviceType;
	private String customerName;
	private String customerMobile;
	private String appointmentFormDate;
	private String appointmentToDate;
	private Integer page;
	private Integer size;
	private Integer profitCenterId;
	private Integer dealerId;
	private Integer branchId;
	private Integer orgHierarchyId;
	private Integer zone;
	private Integer stateId;
	private Integer territory;
	
	
}
