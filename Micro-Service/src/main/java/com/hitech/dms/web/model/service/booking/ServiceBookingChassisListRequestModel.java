package com.hitech.dms.web.model.service.booking;

import java.math.BigInteger;
import lombok.Data;

@Data
public class ServiceBookingChassisListRequestModel {
   
	private BigInteger vinId;
	private String chassisNumber;
}
