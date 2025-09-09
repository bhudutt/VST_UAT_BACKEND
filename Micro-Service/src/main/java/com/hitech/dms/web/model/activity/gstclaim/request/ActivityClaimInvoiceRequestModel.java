/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimInvoiceRequestModel {
	private BigInteger activityClaimHdrId;
	private BigInteger activityHdrId;
	private BigInteger activityPlanHdrId;
	private BigInteger dealerId;
	private String activityClaimNumber;
	private BigDecimal gstPer;
	private BigDecimal gstAmnt;
	private BigDecimal totalInvoiceAmnt;
	private Integer plantId; //added on 12-09-2024
	private String customerInvoiceDate;
	private String customerInvoiceNo;
	private String finalSubmitFlag;

	// private List<ActivityClaimInvDtlRequestModel> activityClaimInvDtlList;

}
