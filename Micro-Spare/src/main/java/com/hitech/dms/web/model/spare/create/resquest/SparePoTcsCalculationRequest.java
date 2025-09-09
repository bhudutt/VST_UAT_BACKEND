/**
 * 
 */
package com.hitech.dms.web.model.spare.create.resquest;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoTcsCalculationRequest {
//	private BigInteger dealerId;
//	private BigInteger branchId;
	private BigDecimal tcsPer;
	private BigDecimal totalBaseAmount;
	private BigDecimal totalGstAmount;

}
