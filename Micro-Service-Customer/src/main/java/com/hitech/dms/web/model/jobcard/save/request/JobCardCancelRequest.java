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
public class JobCardCancelRequest {
	private Integer roId;
	private String cancelRemarks;

}
