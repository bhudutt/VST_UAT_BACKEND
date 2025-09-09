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
public class InstallationChecklistResponse {
	private Integer installDtlId;
	private BigInteger id;
	private String checkpointDesc;
	private String status;

}
