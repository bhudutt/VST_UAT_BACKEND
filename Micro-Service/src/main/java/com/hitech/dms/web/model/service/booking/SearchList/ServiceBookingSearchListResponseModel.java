package com.hitech.dms.web.model.service.booking.SearchList;
import java.math.BigInteger;

import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ServiceBookingSearchListResponseModel {
   
	private BigInteger id;
	private String BranchName;
	private String bookingNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private String  bookingDate;
	private String bookingstatus;
	@Column(name="appointmentDate", length = 10)
	private String appointmentDate;
	private String appointmentTime;
	//private BigInteger vinId;
	private String customerName;
	private String sourceOfBooking;
	private String serviceCategory;
	private String serviceType;
	private String remark;
	private String serviceRepairType;
	//private BigInteger placeofServiceId;
    private String chassisNo;
    private String engineNo;
//	private String customerMobileNo;
	private String model;
//	private String placeOfService;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private String callDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
//	private String createdon;
		
}
