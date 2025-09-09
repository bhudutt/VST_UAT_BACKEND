/**
 * 
 */
package com.hitech.dms.web.model.jobcard.edit.request;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EditOutSiderLabourChargeRequest {
	private boolean deleteFlag;
	private int labourCodeId;
	private BigDecimal rate;
	private BigDecimal hour;
	private BigDecimal amount;
}
