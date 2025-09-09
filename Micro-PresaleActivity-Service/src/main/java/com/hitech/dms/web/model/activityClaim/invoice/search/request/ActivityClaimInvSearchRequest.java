/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvSearchRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger orgHierID;
	private String activityClaimInvoiceNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private Integer page;
	private Integer size;
}
