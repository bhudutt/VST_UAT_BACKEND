/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvCreateRequestModel {
	private BigInteger dealerId;
	private BigInteger activityClaimHdrId;
	private String activityClaimNumber;
	private BigDecimal gstPer;
	private BigDecimal gstAmnt;
	private BigDecimal totalInvoiceAmnt;
	private String location;
	private String customerInvoiceNo;
	private String customerInvoiceDate;
	private String finalSubmitFlag;
	private List<ActivityClaimInvDtlCreateRequestModel> activityClaimInvDtlList;
}
