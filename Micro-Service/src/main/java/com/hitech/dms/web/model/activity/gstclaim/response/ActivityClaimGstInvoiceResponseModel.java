/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.response;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimGstInvoiceResponseModel {
	private String msg;
	private Integer statusCode;
	private String activityClaimGstInvoiceNumber;

}
