/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimGstInvSearchRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger orgHierID;
	private String activityClaimInvoiceNumber;	
	private String fromDate;
	private String toDate;
	private String includeInActive;
	private Integer page;
	private Integer size;
	private Integer plantCode;//added on 12-09-24

}
