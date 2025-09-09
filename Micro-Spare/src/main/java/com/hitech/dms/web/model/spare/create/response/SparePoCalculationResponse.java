/**
 * 
 */
package com.hitech.dms.web.model.spare.create.response;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoCalculationResponse {
	private BigDecimal netAmount;
	private BigDecimal itemGstAmount;
	private BigDecimal itemGstPer;
	private BigDecimal totalAmount;
}
