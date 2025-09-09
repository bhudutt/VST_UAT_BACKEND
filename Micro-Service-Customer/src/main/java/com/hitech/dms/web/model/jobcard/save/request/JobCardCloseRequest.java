/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardCloseRequest {
	
	private Integer roId;
	private String closeRemarks;
	
	private String finalAction;
	private String suggestion;

}
