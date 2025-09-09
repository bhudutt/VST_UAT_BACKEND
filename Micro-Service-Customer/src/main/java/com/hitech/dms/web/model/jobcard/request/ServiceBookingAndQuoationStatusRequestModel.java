/**
 * 
 */
package com.hitech.dms.web.model.jobcard.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ServiceBookingAndQuoationStatusRequestModel {
	private BigInteger vinId;
	private Integer serviceBookingFlag;
	private Integer quotationFlag;
	

}
