/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobTechnicianListResponse {
	private BigInteger empId;
	private String empCode;
	private String firstName;
	private String mobileNumber;
	private String DesignationDesc;
}
