/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobServiceTypeResponse {
	private Integer serviceTypeId;
	private Integer serviceCategoryId;
	private String serviceTypeCode;
	private String serviceTypeDesc;

}
