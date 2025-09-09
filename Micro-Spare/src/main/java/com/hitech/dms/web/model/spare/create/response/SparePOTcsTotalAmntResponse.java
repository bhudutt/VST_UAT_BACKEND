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
public class SparePOTcsTotalAmntResponse {
	private BigDecimal totalTcsAmount;
	private BigDecimal totalPoAmnt;
}
