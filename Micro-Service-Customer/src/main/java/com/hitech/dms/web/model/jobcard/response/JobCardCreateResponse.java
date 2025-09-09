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
public class JobCardCreateResponse {
	private Integer vinId;
	private BigInteger roId;
	private String JobCardNumber;
	private String msg;
	private Integer statusCode;

}
