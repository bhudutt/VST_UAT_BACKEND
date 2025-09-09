/**
 * 
 */
package com.hitech.dms.web.model.spare.create.resquest;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoCalculationRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger partId;
	private Integer qty;
}
