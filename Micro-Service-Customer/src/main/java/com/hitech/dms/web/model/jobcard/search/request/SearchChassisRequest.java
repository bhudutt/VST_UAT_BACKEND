/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.request;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SearchChassisRequest {
	private String chassisNumber;
	private String categoryId;
	private String serviceBooking;
	private String invoiceNumber;
	private String quotationNumber;

}
