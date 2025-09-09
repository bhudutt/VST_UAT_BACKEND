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
public class JobRepresentativeResponse {
	
	private BigInteger lookupId;
	private String lookupTypeCode;
	private String  lookupVal;


}
