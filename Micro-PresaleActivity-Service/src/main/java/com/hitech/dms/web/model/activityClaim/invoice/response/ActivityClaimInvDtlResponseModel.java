/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.response;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvDtlResponseModel {
	private String itemCode;
	private String itemDetails;
	private BigDecimal approvedAmount;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal discountPercent;
	private BigDecimal discountAmount;
	private BigDecimal cgstAmount;
	private BigDecimal cgstPercent;
	private BigDecimal sgstAmount;
	private BigDecimal sgstPercent;
	private BigDecimal igstAmount;
	private BigDecimal igstPercent;
	private BigDecimal gstAmount;
	private BigDecimal totalAmount;
	private BigDecimal netAmnt;
	private String glCode;
	private String hsnCode;
	private Date activityActualFromDate;
	private Date activityActualToDate;
}
