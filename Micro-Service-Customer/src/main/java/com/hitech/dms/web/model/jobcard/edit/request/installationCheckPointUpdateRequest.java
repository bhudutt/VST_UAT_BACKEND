/**
 * 
 */
package com.hitech.dms.web.model.jobcard.edit.request;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class installationCheckPointUpdateRequest {

	private int checkPointId;
	private int installDtlId;
	private int isActive;
	
	
}
