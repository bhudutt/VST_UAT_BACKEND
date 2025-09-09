/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobServiceBookingResponse {	
 private BigInteger id;
 private BigInteger branchId;
 private String bookingno;
 private Date bookingDate;
 private String status;
}
